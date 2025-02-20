#!/bin/bash
if [[ -n "$DRY_RUN" ]]; then
  docker() {
    echo [DRYRUN] docker "$@"
  }
  podman() {
    echo [DRYRUN] podman "$@"
  }
  oc() {
    echo [DRYRUN] oc "$@"
  }
fi

get_dockerfile() {
  component=$1
  if [[ -f $component/src/main/docker/Dockerfile.jvm ]]; then
    echo "src/main/docker/Dockerfile.jvm"
  else
    echo "./Dockerfile"
  fi
}

get_approot() {
  component=$1
  if [ "$component" == "rhsm-subscriptions" ]; then
    echo "$PWD"
  else
    echo "$PWD/$component"
  fi
}

# First sed removes leading ".", second sed removes leading "/"
# so that "./swatch-system-conduit" becomes "swatch-system-conduit"
SERVICES="rhsm-subscriptions `(find -name Dockerfile.jvm -exec dirname {} \;; find -regex '.*/Dockerfile' -exec dirname {} \;) | sed 's/^.//' | sed 's#^/##' | sed s'#/.*##'`"
