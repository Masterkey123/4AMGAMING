install panel:

1: sudo su

2: bash <(curl -s https://raw.githubusercontent.com/Jishnu TheGamer/skyport/refs/heads/main/panel)

WINGS/NODE INSTALL:

1: sudo su

2: bash <(curl -s https://raw.githubusercontent.com/Jishnu TheGamer/skyport/refs/heads/main/wings)

3: cd skyportd

4: paste your node configure

5: pm2 start.

docker run -dt --restart=always --name ss-server -p 48736:48736 teddysun/shadowsocks-libev -s 0.0.0.0 -p 48736 -m aes-256-gcm -k YOUR_PASSWORD --fast-open
