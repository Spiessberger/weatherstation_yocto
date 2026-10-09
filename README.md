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

## SDK

Cross-compile Qt apps on the desktop with the image's SDK (Qt 6 libraries for the target, `moc`/`qmlcachegen`/etc. for the host):

```sh
kas-container shell weatherstation.yml:weatherstation.lock.yml -c "bitbake -c populate_sdk weatherstation-image"
./build/tmp/deploy/sdk/poky-glibc-x86_64-weatherstation-image-*-toolchain-*.sh
. /opt/poky/<version>/environment-setup-cortexa76-poky-linux
cmake -B build -G Ninja && cmake --build build
```

Sourcing the environment sets `CMAKE_TOOLCHAIN_FILE`. Without sourcing (e.g. in an IDE), pass
`-DCMAKE_TOOLCHAIN_FILE=<sdk>/sysroots/x86_64-pokysdk-linux/usr/share/cmake/Qt6Toolchain.cmake` instead.

## Qt

- X11 is removed from `DISTRO_FEATURES` and Qt's default platform is `eglfs` (KMS/GBM, Mesa V3D).
- `cage.service` starts the cage Wayland kiosk at boot on the DSI panel, rotated 180° (the panel is mounted upside down).
  `WAYLAND_DISPLAY=/run/wayland-0` is set for all services, so Qt apps started as services ordered `After=cage.service` use Wayland.
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
  recipes-graphics/cage-kiosk cage at boot, rotation, touch/input udev rules
  recipes-graphics/xdg-runtime-dir XDG_RUNTIME_DIR for login shells
```
