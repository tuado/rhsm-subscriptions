/*
 * Copyright Red Hat, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Red Hat trademarks are not licensed under GPLv3. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package org.candlepin.subscriptions.cloudigrade;

import lombok.extern.slf4j.Slf4j;
import org.candlepin.subscriptions.cloudigrade.internal.ApiClient;
import org.candlepin.subscriptions.cloudigrade.internal.Configuration;
import org.candlepin.subscriptions.cloudigrade.internal.api.resources.UsersApi;
import org.candlepin.subscriptions.http.HttpClient;
import org.candlepin.subscriptions.http.HttpClientProperties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.StringUtils;

/** Factory that produces cloudigrade user service clients using configuration. */
@Slf4j
public class CloudigradeInternalUserApiFactory implements FactoryBean<UsersApi> {

  private final HttpClientProperties properties;

  public CloudigradeInternalUserApiFactory(HttpClientProperties properties) {
    this.properties = properties;
  }

  @Override
  public UsersApi getObject() throws Exception {
    if (properties.isUseStub()) {
      log.info("Using stub cloudigrade internal user API client");
      return new StubCloudigradeInternalUserApi();
    }

    ApiClient client = Configuration.getDefaultApiClient();
    client.setHttpClient(
        HttpClient.buildHttpClient(properties, client.getJSON(), client.isDebugging()));

    var url = properties.getUrl();
    if (StringUtils.hasText(url)) {
      log.info("Cloudigrade internal user service URL: {}", url);
      client.setBasePath(url);
    } else {
      log.warn("Cloudigrade internal user service URL not set...");
    }

    client.setBasePath(properties.getUrl());
    return new UsersApi(client);
  }

  @Override
  public Class<?> getObjectType() {
    return UsersApi.class;
  }
}
