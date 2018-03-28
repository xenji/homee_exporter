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
* Narrow the export to a single group (a device can be part of
  multiple groups, you can create an artificial one to you can control
  the visibility of devices)

## Exported Metrics

Here is a list of metrics already exported. If you miss anything, please
look for an open issue or file a new issue.

* Electric energy
* Light/Brightness
* Temperature
* Homee Status (Home, Sleeping, Away, Vacation)
* Binary Sensors
* Motion Alarms
* Battery Levels
* Link Quality to e.g. Netatmo devices

## Usage

Running the exporter needs a recent Java Runtime Environment, version 8.
It should not make a difference if a OpenJDK or Oracle JRE/JDK is used.

This exporter has been tested with macOS and Linux. If anyone tests this
successfully on Windows, please feel free to open a PR and change this
documentation accordingly.

```
$ java -jar homee_exporter.jar -h
usage: homee_exporter [-h] --username USERNAME --password PASSWORD
                      [--bind-host BIND_HOST] [--bind-port BIND_PORT]
                      --homee-id HOMEE_ID [--check-interval CHECK_INTERVAL]
                      [--ping-interval PING_INTERVAL]
                      [--export-group-id EXPORT_GROUP_ID]

required arguments:
  --username USERNAME                 homee username

  --password PASSWORD                 your SHA-512 hashed homee password. You
                                      can generate the hashed password by e.g.
                                      using
                                      https://passwordsgenerator.net/sha512-h
                                      ash-generator/

  --homee-id HOMEE_ID                 The unique ID of your homee (find it on
                                      the bottom of your cube)


optional arguments:
  -h, --help                          show this help message and exit

  --bind-host BIND_HOST               The IP or hostname to bind the web
                                      server to. Default: all interfaces

  --bind-port BIND_PORT               The port to bind the web server to.
                                      Default: 7100

  --check-interval CHECK_INTERVAL     Check interval in seconds. Default: 15

  --ping-interval PING_INTERVAL       The interval in seconds used to ping the
                                      homee. Default: 10

  --export-group-id EXPORT_GROUP_ID   Exports only metrics from the given
                                      group ID. If the group is not set, all
                                      supported devices are exported.
```

#### Systemd

You can find an example systemd configuration in the `src/main/etc` folder.
The directory layout assumes a debian-like system, please adjust the
folder according to the distribution you use.

The configuration parameter are to be set in the `/etc/default/homee_exporter`
environment file and reflect the default values from the cli.

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

## Installation

### Debian package

The package is currently unsigned, I'm working on this.

Install the package:

    #> curl "https://bintray.com/user/downloadSubjectPublicKey?username=xenji" | apt-key add -
    #> echo "deb [arch=noarch] https://dl.bintray.com/xenji/homee-exporter stable main" | sudo tee -a /etc/apt/sources.list
    #> apt-get install homee-exporter

When done, edit the config file:

    #> vim /etc/default/homee_exporter
