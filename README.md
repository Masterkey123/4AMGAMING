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
