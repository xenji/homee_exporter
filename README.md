# Homee Prometheus.io exporter

## Info
This is my own, homegrown exporter for Homee prometheus.io.
It is based on information I found around the net and some reverse
engineering of their web app using a Charles proxy, a bit of nmap and
tcpdump.

This is by far not complete, but at least it is a start.

### What works

* Auto detection if the Homee is in the local LAN via UDP broadcast
* Every restart issues a new authentication token
* Websocket connection to Homee (either local or to their web app)
* Get all electricity meters to report their stuff

### What does not work?
* Planned but not yet implemented:
    * Temperature,
    * Binary states (window/door sensors)
    * ...
* Custom / free form labels

## Usage

Running the exporter needs a recent Java Runtime Environment, version 8.
It should not make a difference if a OpenJDK or Oracle JRE/JDK is used.

This exporter has been tested with macOS and Linux. If anyone tests this
successfully on Windows, please feel free to open a PR and change this
documentation accordingly.

```
$ java -jar build/libs/homee_exporter-27b0a46.dirty-all.jar -h
usage: homee_exporter [-h] --username USERNAME --password PASSWORD
                      [--bind-host BIND_HOST] [--bind-port BIND_PORT]
                      --homee-id HOMEE_ID [--check-interval CHECK_INTERVAL]

required arguments:
  --username USERNAME               homee username

  --password PASSWORD               your SHA-512 hashed homee password. You
                                    can generate the hashed password by e.g.
                                    using
                                    https://passwordsgenerator.net/sha512-has
                                    h-generator/

  --homee-id HOMEE_ID               The unique ID of your homee (find it on
                                    the bottom of your cube)


optional arguments:
  -h, --help                        show this help message and exit

  --bind-host BIND_HOST             The IP or hostname to bind the web server
                                    to. Default: all interfaces

  --bind-port BIND_PORT             The port to bind the web server to.
                                    Default: 7100

  --check-interval CHECK_INTERVAL   Check interval in seconds. Default: 15
```

### Example output
```
# HELP current_energy Tracks the current energy level of a meter
# TYPE current_energy gauge
current_energy{node_id="17",attribute_id="188",name="Rolladen Küche",unit="W",} 0.0
current_energy{node_id="16",attribute_id="175",name="Treppen",unit="W",} 0.0
current_energy{node_id="17",attribute_id="181",name="Rolladen Küche",unit="kWh",} 1.269
current_energy{node_id="10",attribute_id="128",name="Basement IT",unit="kWh",} 511.511
current_energy{node_id="18",attribute_id="192",name="Wallplug Küche Kühlschrank",unit="kWh",} 671.169
current_energy{node_id="18",attribute_id="195",name="Wallplug Küche Kühlschrank",unit="W",} 201.6
current_energy{node_id="10",attribute_id="127",name="Basement IT",unit="W",} 15.59
current_energy{node_id="16",attribute_id="172",name="Treppen",unit="kWh",} 6.98
```