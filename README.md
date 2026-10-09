# weatherstation_yocto

Yocto (wrynose) image for the weatherstation: Raspberry Pi 5 with a Waveshare 10.1" DSI touch panel,
systemd, NetworkManager and Qt 6.12 (QtQuick) running fullscreen on eglfs.

## Build

Builds in a container using [kas](https://kas.readthedocs.io/):

```sh
kas-container build weatherstation.yml:weatherstation.lock.yml
```

`weatherstation.lock.yml` pins every layer to a commit. Refresh it with `kas-container lock weatherstation.yml`.

## Flash

```sh
cd build/tmp/deploy/images/raspberrypi5
sudo bmaptool copy weatherstation-image-raspberrypi5.rootfs.wic.bz2 /dev/sdX
```

The board comes up as `weatherstation`. Log in as `root` over SSH with no password (development image).

## Qt

- X11 and Wayland are removed from `DISTRO_FEATURES`, so every Qt app defaults to the `eglfs` platform (KMS/GBM, Mesa V3D).
- The Pi 5 has three DRM cards. `/etc/qt-eglfs-kms.json` selects the RP1 DSI card (the panel), and
  `/etc/profile.d/qt-eglfs.sh` exports `QT_QPA_EGLFS_KMS_CONFIG` for login shells.
  Apps started from systemd or a non-login shell must set that variable themselves.
- Roboto is the only installed font.
- To try a QML file on the device, run `qml file.qml`.

## Layout

```
weatherstation.yml           kas config: layers, machine, local.conf
weatherstation.lock.yml      pinned layer commits
meta-weatherstation/
  recipes-core/images/       weatherstation-image
  recipes-qt/qt-eglfs-config eglfs KMS device config
```
