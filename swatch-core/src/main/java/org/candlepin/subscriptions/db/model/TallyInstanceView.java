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
package org.candlepin.subscriptions.db.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.candlepin.subscriptions.json.Measurement;
import org.springframework.data.annotation.Immutable;

@Setter
@Getter
@Entity
@Immutable
@Table(name = "tally_instance_view")
public class TallyInstanceView implements Serializable {

  @EmbeddedId private TallyInstanceViewKey key;

  @Column(name = "id")
  private String id;

  @Column(name = "host_billing_provider")
  private BillingProvider hostBillingProvider;

  @Column(name = "host_billing_account_id")
  private String hostBillingAccountId;

  @NotNull
  @Column(name = "display_name", nullable = false)
  private String displayName;

  @NotNull
  @Column(name = "org_id")
  private String orgId;

  @Column(name = "num_of_guests")
  private Integer numOfGuests;

  @Column(name = "last_seen")
  private OffsetDateTime lastSeen;

  private int cores;

  private int sockets;

  private Double value;

  @Column(name = "subscription_manager_id")
  private String subscriptionManagerId;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "instance_monthly_totals",
      joinColumns = @JoinColumn(name = "instance_id", referencedColumnName = "id"))
  @Column(name = "value")
  private Map<InstanceMonthlyTotalKey, Double> monthlyTotals = new HashMap<>();

  public TallyInstanceView() {
    key = new TallyInstanceViewKey();
  }

  public Double getMonthlyTotal(String monthId, Measurement.Uom uom) {
    var totalKey = new InstanceMonthlyTotalKey(monthId, uom);
    return monthlyTotals.get(totalKey);
  }
}
