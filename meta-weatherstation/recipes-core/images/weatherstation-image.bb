SUMMARY = "Weatherstation image"
LICENSE = "MIT"

inherit core-image

IMAGE_FEATURES += " \
    ssh-server-openssh \
    empty-root-password \
    allow-empty-password \
    allow-root-login \
"

IMAGE_INSTALL += " \
    networkmanager \
    networkmanager-nmcli \
    networkmanager-wifi \
"

