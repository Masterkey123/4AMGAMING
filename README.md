{
  "log": {
    "level": "warn",
    "output": "box.log",
    "timestamp": true
  },
  "dns": {
    "servers": [
      {
        "tag": "dns-remote",
        "address": "udp://1.1.1.1",
        "address_resolver": "dns-direct"
      },
      {
        "tag": "dns-trick-direct",
        "address": "https://sky.rethinkdns.com/",
        "detour": "direct-fragment"
      },
      {
        "tag": "dns-direct",
        "address": "1.1.1.1",
        "address_resolver": "dns-local",
        "detour": "direct"
      },
      {
        "tag": "dns-local",
        "address": "local",
        "detour": "direct"
      },
      {
        "tag": "dns-block",
        "address": "rcode://success"
      }
    ],
    "rules": [
      {
        "domain": "f.khilei.com",
        "server": "dns-direct"
      },
      {
        "domain": "cp.cloudflare.com",
        "server": "dns-remote",
        "rewrite_ttl": 3000
      }
    ],
    "final": "dns-remote",
    "static_ips": {
      "sky.rethinkdns.com": [
        "104.17.148.22",
        "104.17.147.22",
        "104.21.83.62",
        "172.67.214.246",
        "2606:4700:3030::ac43:d6f6",
        "2606:4700:3030::6815:533e"
      ]
    },
    "independent_cache": true
  },
  "inbounds": [
    {
      "type": "tun",
      "tag": "tun-in",
      "mtu": 9000,
      "inet4_address": "172.19.0.1/28",
      "auto_route": true,
      "strict_route": true,
      "endpoint_independent_nat": true,
      "stack": "mixed",
      "sniff": true,
      "sniff_override_destination": true
    },
    {
      "type": "mixed",
      "tag": "mixed-in",
      "listen": "127.0.0.1",
      "listen_port": 12334,
      "sniff": true,
      "sniff_override_destination": true
    },
    {
      "type": "direct",
      "tag": "dns-in",
      "listen": "127.0.0.1",
      "listen_port": 16450
    }
  ],
  "outbounds": [
    {
      "type": "selector",
      "tag": "select",
      "outbounds": [
        "auto",
        "United States-148",
        "United Kingdom-4",
        "United States-208",
        "United States-235",
        "Singapore-8",
        "United States-320",
        "Germany-5",
        "United States-365",
        "United States-369",
        "United Kingdom-9",
        "United States-408",
        "United States-416",
        "United States-417"
      ],
      "default": "auto"
    },
    {
      "type": "urltest",
      "tag": "auto",
      "outbounds": [
        "United States-148",
        "United Kingdom-4",
        "United States-208",
        "United States-235",
        "Singapore-8",
        "United States-320",
        "Germany-5",
        "United States-365",
        "United States-369",
        "United Kingdom-9",
        "United States-408",
        "United States-416",
        "United States-417"
      ],
      "url": "http://connectivitycheck.gstatic.com/generate_204",
      "interval": "10m0s",
      "idle_timeout": "1h40m0s"
    },
    {
      "type": "vmess",
      "tag": "United States-148",
      "server": "162.251.62.115",
      "server_port": 22324,
      "uuid": "04621bae-ab36-11ec-b909-0242ac120002",
      "security": "auto"
    },
    {
      "type": "vmess",
      "tag": "United Kingdom-4",
      "tls_fragment": {
        "enabled": true,
        "size": "10-30",
        "sleep": "2-8"
      },
      "server": "149.7.16.102",
      "server_port": 443,
      "uuid": "03fcc618-b93d-6796-6aed-8a38c975d581",
      "security": "auto",
      "tls": {
        "enabled": true,
        "server_name": "wrmelmwxlf.gktevlrqznwqqozy.fabpfs66gizmnojhcvqxwl.kytrcfzqla87gvgvs6c7kjnrubuh.cc",
        "insecure": true,
        "utls": {
          "enabled": true
        }
      },
      "transport": {
        "type": "ws",
        "path": "/linkvws",
        "headers": {
          "Host": "wrmelmwxlf.gktevlrqznwqqozy.fabpfs66gizmnojhcvqxwl.kytrcfzqla87gvgvs6c7kjnrubuh.cc"
        }
      }
    },
    {
      "type": "vmess",
      "tag": "United States-208",
      "tls_fragment": {
        "enabled": true,
        "size": "10-30",
        "sleep": "2-8"
      },
      "server": "f.khilei.com",
      "server_port": 443,
      "uuid": "03fcc618-b93d-6796-6aed-8a38c975d581",
      "security": "auto",
      "tls": {
        "enabled": true,
        "server_name": "f.khilei.com",
        "insecure": true,
        "utls": {
          "enabled": true
        }
      },
      "transport": {
        "type": "ws",
        "path": "/linkvws",
        "headers": {
          "Host": "f.khilei.com"
        }
      }
    },
    {
      "type": "vmess",
      "tag": "United States-235",
      "tls_fragment": {
        "enabled": true,
        "size": "10-30",
        "sleep": "2-8"
      },
      "server": "107.167.20.181",
      "server_port": 30001,
      "uuid": "418048af-a293-4b99-9b0c-98ca3580dd24",
      "security": "auto",
      "alter_id": 64,
      "tls": {
        "enabled": true,
        "server_name": "www.15314695.xyz",
        "insecure": true,
        "utls": {
          "enabled": true
        }
      },
      "transport": {
        "type": "ws",
        "path": "/path/191019072927",
        "headers": {
          "Host": "www.15314695.xyz"
        }
      }
    },
    {
      "type": "vmess",
      "tag": "Singapore-8",
      "server": "31.192.234.7",
      "server_port": 12954,
      "uuid": "8259cb1c-dd6c-4739-9c88-af550d977525",
      "security": "auto",
      "tls": {
        "enabled": true,
        "server_name": "31.192.234.7",
        "insecure": true,
        "utls": {
          "enabled": true
        }
      }
    },
    {
      "type": "vmess",
      "tag": "United States-320",
      "tls_fragment": {
        "enabled": true,
        "size": "10-30",
        "sleep": "2-8"
      },
      "server": "172.67.213.124",
      "server_port": 443,
      "uuid": "03fcc618-b93d-6796-6aed-8a38c975d581",
      "security": "auto",
      "tls": {
        "enabled": true,
        "server_name": "tilebani.com",
        "insecure": true,
        "utls": {
          "enabled": true
        }
      },
      "transport": {
        "type": "ws",
        "path": "/linkvws",
        "headers": {
          "Host": "tilebani.com"
        }
      }
    },
    {
      "type": "vmess",
      "tag": "Germany-5",
      "tls_fragment": {
        "enabled": true,
        "size": "10-30",
        "sleep": "2-8"
      },
      "server": "130.162.47.123",
      "server_port": 6854,
      "uuid": "b5f7e932-f730-4ad2-b425-40977b9db332",
      "security": "auto",
      "tls": {
        "enabled": true,
        "server_name": "130.162.47.123",
        "insecure": true,
        "utls": {
          "enabled": true
        }
      },
      "transport": {
        "type": "ws",
        "path": "/zBLPou",
        "headers": {
          "Host": "130.162.47.123"
        }
      }
    },
    {
      "type": "vmess",
      "tag": "United States-365",
      "server": "38.99.82.160",
      "server_port": 22324,
      "uuid": "04621bae-ab36-11ec-b909-0242ac120002",
      "security": "auto"
    },
    {
      "type": "vmess",
      "tag": "United States-369",
      "tls_fragment": {
        "enabled": true,
        "size": "10-30",
        "sleep": "2-8"
      },
      "server": "77.83.141.171",
      "server_port": 443,
      "uuid": "35379219-6535-4f2e-a4fe-3e44f61e0eee",
      "security": "auto",
      "alter_id": 32,
      "tls": {
        "enabled": true,
        "server_name": "vc.fly.dev",
        "insecure": true,
        "utls": {
          "enabled": true
        }
      },
      "transport": {
        "type": "ws",
        "path": "/vc",
        "headers": {
          "Host": "vc.fly.dev"
        }
      }
    },
    {
      "type": "vmess",
      "tag": "United Kingdom-9",
      "tls_fragment": {
        "enabled": true,
        "size": "10-30",
        "sleep": "2-8"
      },
      "server": "149.7.16.73",
      "server_port": 443,
      "uuid": "03fcc618-b93d-6796-6aed-8a38c975d581",
      "security": "auto",
      "tls": {
        "enabled": true,
        "server_name": "149.7.16.73",
        "insecure": true,
        "utls": {
          "enabled": true
        }
      },
      "transport": {
        "type": "ws",
        "path": "/linkvws",
        "headers": {
          "Host": "149.7.16.73"
        }
      }
    },
    {
      "type": "vmess",
      "tag": "United States-408",
      "tls_fragment": {
        "enabled": true,
        "size": "10-30",
        "sleep": "2-8"
      },
      "server": "38.99.82.203",
      "server_port": 443,
      "uuid": "03fcc618-b93d-6796-6aed-8a38c975d581",
      "security": "auto",
      "tls": {
        "enabled": true,
        "server_name": "38.99.82.203",
        "insecure": true,
        "utls": {
          "enabled": true
        }
      },
      "transport": {
        "type": "ws",
        "path": "/linkvws",
        "headers": {
          "Host": "38.99.82.203"
        }
      }
    },
    {
      "type": "vmess",
      "tag": "United States-416",
      "tls_fragment": {
        "enabled": true,
        "size": "10-30",
        "sleep": "2-8"
      },
      "server": "104.26.12.72",
      "server_port": 443,
      "uuid": "03fcc618-b93d-6796-6aed-8a38c975d581",
      "security": "auto",
      "tls": {
        "enabled": true,
        "server_name": "cake.capellare.com",
        "insecure": true,
        "utls": {
          "enabled": true
        }
      },
      "transport": {
        "type": "ws",
        "path": "/linkvws",
        "headers": {
          "Host": "cake.capellare.com"
        }
      }
    },
    {
      "type": "vmess",
      "tag": "United States-417",
      "tls_fragment": {
        "enabled": true,
        "size": "10-30",
        "sleep": "2-8"
      },
      "server": "104.21.49.154",
      "server_port": 443,
      "uuid": "05641cf5-58d2-4ba4-a9f1-b3cda0b1fb1d",
      "security": "auto",
      "tls": {
        "enabled": true,
        "server_name": "nezuko.raidenshogun.cloudns.org",
        "insecure": true,
        "utls": {
          "enabled": true
        }
      },
      "transport": {
        "type": "ws",
        "path": "/linkws/obdii.cfd",
        "headers": {
          "Host": "nezuko.raidenshogun.cloudns.org"
        }
      }
    },
    {
      "type": "dns",
      "tag": "dns-out"
    },
    {
      "type": "direct",
      "tag": "direct"
    },
    {
      "type": "direct",
      "tag": "direct-fragment",
      "tls_fragment": {
        "enabled": true,
        "size": "10-30",
        "sleep": "2-8"
      }
    },
    {
      "type": "direct",
      "tag": "bypass"
    },
    {
      "type": "block",
      "tag": "block"
    }
  ],
  "route": {
    "rules": [
      {
        "inbound": "dns-in",
        "outbound": "dns-out"
      },
      {
        "port": 53,
        "outbound": "dns-out"
      },
      {
        "clash_mode": "Direct",
        "outbound": "direct"
      },
      {
        "clash_mode": "Global",
        "outbound": "select"
      }
    ],
    "final": "select",
    "auto_detect_interface": true,
    "override_android_vpn": true
  },
  "experimental": {
    "cache_file": {
      "enabled": true,
      "path": "clash.db"
    },
    "clash_api": {
      "external_controller": "127.0.0.1:16756",
      "secret": "HP32nvFk4876gFV8"
    }
  }
}
dHJvamFuOi8vY2FiY2FiYS1hY2FiLWFjYmEtYWRiYy1hY2JjY2NiYWJhYmFAMTI3LjAuMC4xOjEwODA/c2VjdXJpdHk9dGxzJnR5cGU9dGNwI/CflIQgTEFURVNULVVQREFURSDwn5OFIFRIVS0xMC1BQkFOLTE0MDMg8J+VkSAyMDoyMwpzczovL1kyaGhZMmhoTWpBdGFXVjBaaTF3YjJ4NU1UTXdOVG8yWVRnMVlUY3lZeTFqTnpkbExUUXdNelF0T1RNNE5DMW1ZbUZsTURRNU5UQXhNMlE9QDQ1LjE1OC41OS43NDo0Nzk2OSPwn5SSIFNTLVRDUC1OQSDwn4e48J+HrCBTRy00NS4xNTguNTkuNzQ6NDc5NjkKc3M6Ly9ZMmhoWTJoaE1qQXRhV1YwWmkxd2IyeDVNVE13TlRwYVpGZGFPVkYyUm14aVQwOUhjbVZOYjBVelFuRTRAMTU5LjY1LjEyOS4xMjg6MTg4MTQj8J+UkiBTUy1UQ1AtTkEg8J+HuPCfh6wgU0ctMTU5LjY1LjEyOS4xMjg6MTg4MTQKc3M6Ly9ZMmhoWTJoaE1qQXRhV1YwWmkxd2IyeDVNVE13TlRwVWQyNXZZakUyYmtoRE1rWldNRFZLV2tWNU5YQTVAMTc0LjEzOC4xNy4yNTQ6NTIyNTIj8J+UkiBTUy1UQ1AtTkEg8J+HuPCfh6wgU0ctMTc0LjEzOC4xNy4yNTQ6NTIyNTIKc3M6Ly9ZV1Z6TFRJMU5pMWpabUk2WVcxaGVtOXVjMnR5TURVPUAxMy4yMTMuMzAuMjQyOjQ0MyPwn5SSIFNTLVRDUC1OQSDwn4e48J+HrCBTRy0xMy4yMTMuMzAuMjQyOjQ0MwpzczovL1lXVnpMVEkxTmkxalptSTZZVzFoZW05dWMydHlNRFU9QDU0LjI1NS4yMzcuMjQ5OjQ0MyPwn5SSIFNTLVRDUC1OQSDwn4e48J+HrCBTRy01NC4yNTUuMjM3LjI0OTo0NDMKc3M6Ly9ZV1Z6TFRJMU5pMW5ZMjA2WXpaa016VXdPVFF0WkdRNE5TMDBNakF5TFRneFpXWXROV1V6T0Rkak5UWXdORGd4QDE0MC4yNDUuNTcuMzM6MjE4NTAj8J+UkiBTUy1UQ1AtTkEg8J+HuPCfh6wgU0ctMTQwLjI0NS41Ny4zMzoyMTg1MAp0cm9qYW46Ly8wZDg5YTM2ZS0zNmJjLTQxZDItOGJkMy1iYmQ2Mzk5OTFmMjlAMjA2LjIzOC4yMzYuMTg3OjIwOTY/c2VjdXJpdHk9dGxzJnNuaT1uaWNvcm9iaW4uZXVsYS5jbG91ZG5zLm9yZyZ0eXBlPXdzJmhvc3Q9bmljb3JvYmluLmV1bGEuY2xvdWRucy5vcmcj8J+UkiBUUi1XUy1UTFMg8J+HuPCfh6wgU0ctMjA2LjIzOC4yMzYuMTg3OjIwOTYKdm1lc3M6Ly9leUoySWpvZ0lqSWlMQ0FpWVdSa0lqb2dJalV1TXpRdU1UYzJMakV4TVNJc0lDSndiM0owSWpvZ0lqVTNOakUwSWl3Z0ltbGtJam9nSWpSbE5UVmpaVFF3TFRsbU9HVXROREUzWkMxbU9HRmxMV0ZoTm1GbU9UZGhOekE1TnlJc0lDSmhhV1FpT2lBaU1DSXNJQ0p6WTNraU9pQWlZWFYwYnlJc0lDSnVaWFFpT2lBaWRHTndJaXdnSW5SNWNHVWlPaUFpYm05dVpTSXNJQ0pvYjNOMElqb2dJaUlzSUNKd1lYUm9Jam9nSWlJc0lDSjBiSE1pT2lBaUlpd2dJbk51YVNJNklDSWlMQ0FpWVd4d2JpSTZJQ0lpTENBaVpuQWlPaUFpSWl3Z0luQnpJam9nSWx4MVpEZ3paRngxWkdReE1pQldUUzFVUTFBdFRrRWdYSFZrT0ROalhIVmtaR1k0WEhWa09ETmpYSFZrWkdWaklGTkhMVFV1TXpRdU1UYzJMakV4TVRvMU56WXhOQ0o5CnZsZXNzOi8vOWE0YmMxNWQtYTdkNC00MTgxLTk0ODUtNDA1NjI5YzM2ODk0QDIwNi4yMzguMjM3LjQ5Ojg0NDM/c2VjdXJpdHk9dGxzJnNuaT1raW5nLWFlMS5wYWdlcy5kZXYmdHlwZT13cyZob3N0PWtpbmctYWUxLnBhZ2VzLmRldiZwYXRoPSUyRiU0MEFaQVJCQVlKQUIxJTQwQVpBUkJBWUpBQjElNDBBWkFSQkFZSkFCMSU0MEFaQVJCQVlKQUIxJTQwQVpBUkJBWUpBQjElNDBBWkFSQkFZSkFCMSUzRmVkJTNEMjU2MCPwn5SSIFZMLVdTLVRMUyDwn4e48J+HrCBTRy0yMDYuMjM4LjIzNy40OTo4NDQzCnZsZXNzOi8vYTEzZGY5NDAtMDIwYy00NjVmLWJjODktZWU1Mjc5YjVjZDZhQDIwNi4yMzguMjM3Ljg4OjQ0Mz9zZWN1cml0eT10bHMmc25pPWRkLnlsa3MueHl6JnR5cGU9d3MmaG9zdD1kZC55bGtzLnh5eiZwYXRoPSUyRiUzRnByb3h5aXAlM0Rwcm94eWlwLnNnLmZ4eGsuZGVkeW4uaW8j8J+UkiBWTC1XUy1UTFMg8J+HuPCfh6wgU0ctMjA2LjIzOC4yMzcuODg6NDQzCnZsZXNzOi8vZTJkZDBhNzktODkyYy00OTc4LTkyYjAtZGVjYWRhYWUyNWVlQDIwNi4xODkuMTU2LjIwOTo4MD9zZWN1cml0eT1ub25lJnR5cGU9d3MmaG9zdD16b29tLnVzJnBhdGg9JTJGRlJFRStCeStUaGFydXdhKyUyODA3Njc1OTczMTclMjklMkYrJTQwel96X3pfenpfeiPwn5SSIFZMLVdTLU5PTkUg8J+HuPCfh6wgU0ctMjA2LjE4OS4xNTYuMjA5OjgwCnZsZXNzOi8vMGQ4OWEzNmUtMzZiYy00MWQyLThiZDMtYmJkNjM5OTkxZjI5QDIwNi4yMzguMjM2LjE4NzoyMDk2P3NlY3VyaXR5PXRscyZzbmk9bmljb3JvYmluLmV1bGEuY2xvdWRucy5vcmcmdHlwZT13cyZob3N0PW5pY29yb2Jpbi5ldWxhLmNsb3VkbnMub3JnJnBhdGg9JTJGVGVsZWdyYW0lNDBMaW5rdndzJTNGZWQlM0QyMDQ4I/CflJIgVkwtV1MtVExTIPCfh7jwn4esIFNHLTIwNi4yMzguMjM2LjE4NzoyMDk2CnZsZXNzOi8vNjVkZjU2N2ItYmM0ZC00YjJmLWE0ZTMtNzRlYTY4NmUyNzIxQDkyLjI0My43NC4yOjQ0Mz9zZWN1cml0eT10bHMmc25pPXllbGFhYW4udGhlbGFzdHJvbmluLmNsb3VkbnMub3JnJnR5cGU9d3MmaG9zdD15ZWxhbi50aGVsYXN0cm9uaW4uY2xvdWRucy5vcmcmcGF0aD0lMkZWTEVTUyUyRnNlLWZ1bGwucHJpdmF0ZWlwLm5ldCZmcD1yYW5kb20mYWxwbj1odHRwJTJGMS4xI/CflJIgVkwtV1MtVExTIPCfh7jwn4esIFNHLTkyLjI0My43NC4yOjQ0Mwp2bGVzczovL2ExM2RmOTQwLTAyMGMtNDY1Zi1iYzg5LWVlNTI3OWI1Y2Q2YUAxNTguMTc4LjIzOC4yMzk6NDQzP3NlY3VyaXR5PXRscyZzbmk9bHYua3BjbG91ZC5ldS5vcmcmdHlwZT13cyZwYXRoPSUyRiUzRmVkJTNEMjA0OCPwn5SSIFZMLVdTLVRMUyDwn4e48J+HrCBTRy0xNTguMTc4LjIzOC4yMzk6NDQzCnZsZXNzOi8vMjY3YjE5ZTAtODg0OS00MWMyLWFhM2MtNTBjY2Q0NDc4YjMxQDE5OS4yMzIuMTUuMjM5OjQ0Mz9zZWN1cml0eT10bHMmc25pPUZ1bGxIRC40Sy5FTGlWMlJhWS5haSZ0eXBlPXdzJmhvc3Q9ZnVsbGhkLjRrLkVMaVYyckFZLmFpJnBhdGg9JTJGVkxFU1MlM0YtRUxpVjJyQVktRUxpVjJyQVkmZnA9ZWRnZSPwn5SSIFZMLVdTLVRMUyDwn4e48J+HrCBTRy0xOTkuMjMyLjE1LjIzOTo0NDMKdmxlc3M6Ly9hZGY4NDZmYS0wNGNjLTRiZWMtYTY3ZC1lMzZmOGE0OGJjODNAMTUyLjQyLjE5Mi4yMjY6NDQzP3NlY3VyaXR5PXRscyZzbmk9RU1QVFkmdHlwZT10Y3AmYWxwbj1oMyUyQ2gyJTJDaHR0cCUyRjEuMSZhbGxvd0luc2VjdXJlPTEj8J+UkiBWTC1UQ1AtVExTIPCfh7jwn4esIFNHLTE1Mi40Mi4xOTIuMjI2OjQ0Mwp2bGVzczovL2ExM2RmOTQwLTAyMGMtNDY1Zi1iYzg5LWVlNTI3OWI1Y2Q2YUAyMDYuMjM4LjIzNy4xMzM6NDQzP3NlY3VyaXR5PXRscyZzbmk9bHgua3BjbG91ZC5ldS5vcmcmdHlwZT13cyZob3N0PWx4LmtwY2xvdWQuZXUub3JnJnBhdGg9JTJGYmx1ZSPwn5SSIFZMLVdTLVRMUyDwn4e48J+HrCBTRy0yMDYuMjM4LjIzNy4xMzM6NDQzCnZsZXNzOi8vZjIwMDQxMjYtNTg2NS00OTgwLWI2ZGQtMjIxMWRiYWFkZjEwQDkyLjIyMy4xMDAuMTUzOjQ0Mz9zZWN1cml0eT10bHMmc25pPWdjb3JlLmNvbSZ0eXBlPXdzJmhvc3Q9MTguZ2NyLmRuczA0LmNvbSZwYXRoPSUyRiUzRmVkJTNEMjU2MCZmcD1jaHJvbWUj8J+UkiBWTC1XUy1UTFMg8J+HuPCfh6wgU0ctOTIuMjIzLjEwMC4xNTM6NDQzCnZsZXNzOi8vZjIwMDQxMjYtNTg2NS00OTgwLWI2ZGQtMjIxMWRiYWFkZjEwQDkyLjIyMy4xMDAuNTM6NDQzP3NlY3VyaXR5PXRscyZzbmk9Z2NvcmUuY29tJnR5cGU9d3MmaG9zdD0xOC5nY3IuZG5zMDQuY29tJnBhdGg9JTJGJTNGZWQlM0QyNTYwJmZwPWNocm9tZSPwn5SSIFZMLVdTLVRMUyDwn4e48J+HrCBTRy05Mi4yMjMuMTAwLjUzOjQ0Mwp2bGVzczovL2YwZDU4NGJjLTM0MmYtNDQ0My1iYTI0LTc5ZTRkMGU1MDRiMUAyMDYuMjM4LjIzNi41Mzo4MD9zZWN1cml0eT1ub25lJnR5cGU9d3MmaG9zdD1sYXRlLXRvb3RoLmdyYW56aW5oYW5uYWgud29ya2Vycy5kZXYmcGF0aD0lMkYlNDBBWkFSQkFZSkFCMSU0MEFaQVJCQVlKQUIxJTQwQVpBUkJBWUpBQjElM0ZlZCPwn5SSIFZMLVdTLU5PTkUg8J+HuPCfh6wgU0ctMjA2LjIzOC4yMzYuNTM6ODAKdmxlc3M6Ly8wZDg5YTM2ZS0zNmJjLTQxZDItOGJkMy1iYmQ2Mzk5OTFmMjlAMjA2LjIzOC4yMzcuNDc6MjA1Mz9zZWN1cml0eT10bHMmc25pPW5pY29yb2Jpbi5ldWxhLmNsb3VkbnMub3JnJnR5cGU9d3MmaG9zdD1uaWNvcm9iaW4uZXVsYS5jbG91ZG5zLm9yZyZwYXRoPSUyRlRlbGVncmFtJTQwTGlua3Z3cyUzRmVkJTNEMjA0OCPwn5SSIFZMLVdTLVRMUyDwn4e48J+HrCBTRy0yMDYuMjM4LjIzNy40NzoyMDUzCnZsZXNzOi8vOWE0YmMxNWQtYTdkNC00MTgxLTk0ODUtNDA1NjI5YzM2ODk0QDIwNi4yMzguMjM3LjM1Ojg0NDM/c2VjdXJpdHk9dGxzJnNuaT1raW5nLWFlMS5wYWdlcy5kZXYmdHlwZT13cyZob3N0PWtpbmctYWUxLnBhZ2VzLmRldiZwYXRoPSUyRiU0MEFaQVJCQVlKQUIxJTQwQVpBUkJBWUpBQjElNDBBWkFSQkFZSkFCMSU0MEFaQVJCQVlKQUIxJTQwQVpBUkJBWUpBQjElNDBBWkFSQkFZSkFCMSUzRmVkJTNEMjU2MCPwn5SSIFZMLVdTLVRMUyDwn4e48J+HrCBTRy0yMDYuMjM4LjIzNy4zNTo4NDQzCnZsZXNzOi8vYTEzZGY5NDAtMDIwYy00NjVmLWJjODktZWU1Mjc5YjVjZDZhQDIwNi4yMzguMjM3LjYwOjQ0Mz9zZWN1cml0eT10bHMmc25pPWx3Lnlsa2EudXMua2cmdHlwZT13cyZwYXRoPSUyRiUzRmVkJTNEMjU2MCPwn5SSIFZMLVdTLVRMUyDwn4e48J+HrCBTRy0yMDYuMjM4LjIzNy42MDo0NDMKdmxlc3M6Ly9hMTNkZjk0MC0wMjBjLTQ2NWYtYmM4OS1lZTUyNzliNWNkNmFAMjA2LjIzOC4yMzcuNzQ6NDQzP3NlY3VyaXR5PXRscyZzbmk9ZHkueWxrcy54eXomdHlwZT13cyZob3N0PWR5Lnlsa3MueHl6JnBhdGg9JTJGUHJveHlJUCUzRFByb3h5SVAuVVMuZnh4ay5kZWR5bi5pbyPwn5SSIFZMLVdTLVRMUyDwn4e48J+HrCBTRy0yMDYuMjM4LjIzNy43NDo0NDMKdmxlc3M6Ly84OWIzY2JiYS1lNmFjLTQ4NWEtOTQ4MS05NzZhMDQxNWVhYjlAMTk5LjIzMi4xMi40MTo0NDM/c2VjdXJpdHk9dGxzJnNuaT1tZWZhLmlyJnR5cGU9d3MmaG9zdD1tZWZhLmlyJnBhdGg9JTJGSCUzRmVkJTNEMjU2MCZmcD1yYW5kb21pemVkJmFscG49aDIsaHR0cC8xLjEj8J+UkiBWTC1XUy1UTFMg8J+HuPCfh6wgU0ctMTk5LjIzMi4xMi40MTo0NDMKdmxlc3M6Ly82NWRmNTY3Yi1iYzRkLTRiMmYtYTRlMy03NGVhNjg2ZTI3MjFAOTIuMjQzLjc0LjM6NDQzP3NlY3VyaXR5PXRscyZzbmk9eWVsYWFhbi50aGVsYXN0cm9uaW4uY2xvdWRucy5vcmcmdHlwZT13cyZob3N0PXllbGFuLnRoZWxhc3Ryb25pbi5jbG91ZG5zLm9yZyZwYXRoPSUyRlZMRVNTJTJGc2UtZnVsbC5wcml2YXRlaXAubmV0JmZwPXJhbmRvbSZhbHBuPWh0dHAlMkYxLjEj8J+UkiBWTC1XUy1UTFMg8J+HuPCfh6wgU0ctOTIuMjQzLjc0LjM6NDQzCnZsZXNzOi8vNjk5YzRkYmQtZTA4Mi00N2NhLWM2MmItOWU5ZGU5MjdkNGI4QDE0Ni4xOTAuMTAxLjIwMjo0NDM/c2VjdXJpdHk9dGxzJnNuaT1ob3N0JnR5cGU9dGNwI/CflJIgVkwtVENQLVRMUyDwn4e48J+HrCBTRy0xNDYuMTkwLjEwMS4yMDI6NDQzCnZsZXNzOi8vOWE0YmMxNWQtYTdkNC00MTgxLTk0ODUtNDA1NjI5YzM2ODk0QDIwNi4yMzguMjM3LjEzMzo4NDQzP3NlY3VyaXR5PXRscyZzbmk9a2luZy1hZTEucGFnZXMuZGV2JnR5cGU9d3MmaG9zdD1raW5nLWFlMS5wYWdlcy5kZXYmcGF0aD0lMkYlNDBBWkFSQkFZSkFCMSU0MEFaQVJCQVlKQUIxJTQwQVpBUkJBWUpBQjElNDBBWkFSQkFZSkFCMSU0MEFaQVJCQVlKQUIxJTQwQVpBUkJBWUpBQjElM0ZlZCUzRDI1NjAj8J+UkiBWTC1XUy1UTFMg8J+HuPCfh6wgU0ctMjA2LjIzOC4yMzcuMTMzOjg0NDMKdmxlc3M6Ly8wZDg5YTM2ZS0zNmJjLTQxZDItOGJkMy1iYmQ2Mzk5OTFmMjlAMjA2LjIzOC4yMzYuOTA6MjA4Nz9zZWN1cml0eT10bHMmc25pPW5pY29yb2Jpbi5ldWxhLmNsb3VkbnMub3JnJnR5cGU9d3MmaG9zdD1uaWNvcm9iaW4uZXVsYS5jbG91ZG5zLm9yZyZwYXRoPSUyRlRlbGVncmFtJTQwTGlua3Z3cyUzRmVkJTNEMjA0OCPwn5SSIFZMLVdTLVRMUyDwn4e48J+HrCBTRy0yMDYuMjM4LjIzNi45MDoyMDg3CnZsZXNzOi8vMGQ4OWEzNmUtMzZiYy00MWQyLThiZDMtYmJkNjM5OTkxZjI5QDIwNi4yMzguMjM3Ljg2OjIwODc/c2VjdXJpdHk9dGxzJnNuaT1uaWNvcm9iaW4uZXVsYS5jbG91ZG5zLm9yZyZ0eXBlPXdzJmhvc3Q9bmljb3JvYmluLmV1bGEuY2xvdWRucy5vcmcmcGF0aD0lMkYlM0ZlZCUzRDIwNDglMjZUZWxlZ3JhbSVGMCU5RiU4NyVBOCVGMCU5RiU4NyVCMyslNDBXYW5nQ2FpMiPwn5SSIFZMLVdTLVRMUyDwn4e48J+HrCBTRy0yMDYuMjM4LjIzNy44NjoyMDg3CnZsZXNzOi8vMGQ4OWEzNmUtMzZiYy00MWQyLThiZDMtYmJkNjM5OTkxZjI5QDIwNi4yMzguMjM3LjE2MjoyMDgzP3NlY3VyaXR5PXRscyZzbmk9bmljb3JvYmluLmV1bGEuY2xvdWRucy5vcmcmdHlwZT13cyZob3N0PW5pY29yb2Jpbi5ldWxhLmNsb3VkbnMub3JnJnBhdGg9JTJGJTQwQVpBUkJBWUpBQjElNDBBWkFSQkFZSkFCMSU0MEFaQVJCQVlKQUIxJTNGZWQlM0QyMDQ4I/CflJIgVkwtV1MtVExTIPCfh7jwn4esIFNHLTIwNi4yMzguMjM3LjE2MjoyMDgzCnZsZXNzOi8vNzk3NzVlNDktYzNkNy00NTM1LTk3ZTUtNjk4MGRiZjc2MGQxQDkyLjI0My43NC4xMzc6ODQ0Mz9zZWN1cml0eT10bHMmc25pPXZpcDQtcm9tYXgtdGlrdG9rLmdvbGFieWF6ZC5pciZ0eXBlPXdzJmhvc3Q9dmlwNC1yb21heC10aWt0b2suZ29sYWJ5YXpkLmlyJmZwPXJhbmRvbSPwn5SSIFZMLVdTLVRMUyDwn4e48J+HrCBTRy05Mi4yNDMuNzQuMTM3Ojg0NDMKdmxlc3M6Ly85YTRiYzE1ZC1hN2Q0LTQxODEtOTQ4NS00MDU2MjljMzY4OTRAMjA2LjIzOC4yMzcuNDA6ODQ0Mz9zZWN1cml0eT10bHMmc25pPWtpbmctYWUxLnBhZ2VzLmRldiZ0eXBlPXdzJmhvc3Q9a2luZy1hZTEucGFnZXMuZGV2JnBhdGg9JTJGJTQwQVpBUkJBWUpBQjElNDBBWkFSQkFZSkFCMSU0MEFaQVJCQVlKQUIxJTQwQVpBUkJBWUpBQjElNDBBWkFSQkFZSkFCMSU0MEFaQVJCQVlKQUIxJTNGZWQlM0QyNTYwI/CflJIgVkwtV1MtVExTIPCfh7jwn4esIFNHLTIwNi4yMzguMjM3LjQwOjg0NDMKdmxlc3M6Ly9hMTNkZjk0MC0wMjBjLTQ2NWYtYmM4OS1lZTUyNzliNWNkNmFAMjA2LjIzOC4yMzYuODM6NDQzP3NlY3VyaXR5PXRscyZzbmk9bHgueWxrczAxLmV1Lm9yZyZ0eXBlPXdzJmhvc3Q9bHgueWxrczAxLmV1Lm9yZyZwYXRoPSUyRmJsdWUj8J+UkiBWTC1XUy1UTFMg8J+HuPCfh6wgU0ctMjA2LjIzOC4yMzYuODM6NDQzCnZsZXNzOi8vYTEzZGY5NDAtMDIwYy00NjVmLWJjODktZWU1Mjc5YjVjZDZhQDIwNi4yMzguMjM3LjE4Mjo0NDM/c2VjdXJpdHk9dGxzJnNuaT1keS55bGtzLnh5eiZ0eXBlPXdzJmhvc3Q9ZHkueWxrcy54eXomcGF0aD0lMkZQcm94eUlQJTNEUHJveHlJUC5VUy5meHhrLmRlZHluLmlvI/CflJIgVkwtV1MtVExTIPCfh7jwn4esIFNHLTIwNi4yMzguMjM3LjE4Mjo0NDMKdHJvamFuOi8vYWJjYmFjYS1hYWNiLWNhYmEtZGFiYy1iYWFjY2NiY2FhYmJAMTI3LjAuMC4xOjgwODA/c2VjdXJpdHk9dGxzJnR5cGU9dGNwI/Cfkajwn4+74oCN8J+SuyBERVZFTE9QRUQtQlkgU09ST1VTSC1NSVJaQUVJIPCfk4wgRk9MTE9XLUNPTlRBQ1QgU1lEU1JTTVJa
