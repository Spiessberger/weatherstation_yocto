# Without PAM, logind sets no XDG_RUNTIME_DIR for SSH/console logins; Wayland needs one
if [ -z "$XDG_RUNTIME_DIR" ]; then
    export XDG_RUNTIME_DIR=/run/user/$(id -u)
    mkdir -p -m 0700 "$XDG_RUNTIME_DIR"
fi
