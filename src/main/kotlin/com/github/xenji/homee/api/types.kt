package com.github.xenji.homee.api

/** Users */
val users_role = mapOf(
    1 to "Service",
    2 to "Admin",
    3 to "Standard",
    4 to "Limited"
)
val users_type = mapOf(
    0 to "None",
    1 to "Local",
    2 to "MyHager"
)

/** Notifications */
val notification_node = mapOf(
    1 to "BatteryLow",
    2 to "BadLinkQuality",
    3 to "IsMissing"
)
val notification_warning = mapOf(
    1 to "SmokeDetected",
    2 to "WaterDetected"
)
val notification_update = mapOf(
    1 to "Available",
    2 to "InProgrss",
    3 to "Finished",
    4 to "Issue"
)
val notification_cube = mapOf(
    1 to "Added",
    2 to "Removed",
    3 to "IsMissing"
)

/** Devices */
val devices_type = mapOf(
    0 to "None",
    1 to "Phone",
    2 to "Tablet",
    3 to "Desktop",
    4 to "Browser"
)
val devices_os = mapOf(
    0 to "None",
    1 to "iOS",
    2 to "Android",
    3 to "Windows",
    4 to "WindowsPhone",
    5 to "Linux",
    6 to "MacOs"
)
/** Nodes */
val nodes_profile = mapOf(
    0 to "None",
    1 to "Homee Cube",
    // Generic (10-999)
    10 to "OnOffPlug",
    11 to "DimmableMeteringSwitch",
    12 to "MeteringSwitch",
    13 to "MeteringPlug",
    14 to "DimmablePlug",
    15 to "DimmableSwitch",
    16 to "OnOffSwitch",
    18 to "DoubleOnOffSwitch",
    19 to "DimmableMeteringPlug",
    20 to "OneButtonRemote",
    21 to "BinaryInput",
    22 to "DimmableColorMeteringSwitch",
    23 to "DoubleBinaryInput",
    24 to "TwoButtonRemote",
    25 to "ThreeButtonRemote",
    26 to "FourButtonRemote",
    27 to "AlarmSensor",
    // Lighting (1000 - 1999
    1000 to "BrightnessSensor",
    1001 to "DimmableColorLight",
    1002 to "DimmableExtendedColorLight",
    1003 to "DimmableColorTemperatureLight",
    // Closures (2000 - 2999)
    2000 to "OpenCloseSensor",
    2001 to "WindowHandle",
    2002 to "ShutterPositionSwitch",
    2003 to "OpenCloseAndTemperatureSensor",
    2004 to "ElectricMotorMeteringSwitch",
    2005 to "??",
    // HVAC (3000 - 3999)
    3001 to "TemperatureAndHumiditySensor",
    3002 to "CO2Sensor",
    3003 to "RoomThermostat",
    3004 to "RoomThermostatWithHumiditySensor",
    3005 to "BinaryInputWithTemperatureSensor",
    3006 to "RadiatorThermostat",
    3009 to "TemperatureSensor",
    3010 to "HumiditySensor",
    3011 to "WaterValve",
    3012 to "WaterMeter",
    3013 to "WeatherStation",
    3014 to "NetatmoMainModule",
    3015 to "NetatmoOutdoorModule",
    3016 to "NetatmoIndoorModule",
    3017 to "NetatmoRainModule",
    3018 to "TwoChannelCosiTherm",
    3019 to "SixChannelCosiTherm",
    3020 to "NestThermostatWithCoolinng",
    3021 to "NestThermostatWithHeating",
    3022 to "NestThermostatWithHeatingAndCooling",
    3023 to "NetatmoWindModule",
    // Alarm (4000 - 4999)
    4010 to "MotionDetectorWithTemperatureBrightnessAndHumiditySensor",
    4011 to "MotionDetector",
    4012 to "SmokeDetector",
    4013 to "FloodDetector",
    4014 to "PresenceDetector",
    4015 to "MotionDetectorWithTemperatureAndBrightnessSensor",
    4016 to "SmokeDetectorWithTemperatureSensor",
    4017 to "FloodDetectorWithTemperatureSensor",
    4018 to "WatchDogDevice",
    4019 to "LAG",
    4020 to "OWU",
    4021 to "Eurovac",
    4022 to "OWWG3",
    4023 to "Europress",
    4024 to "MinimumDetector",
    4025 to "MaximumDetector",
    4026 to "SmokeDetectorAndCODetector",
    // Hager (5000 - 5999)
    5000 to "InovaAlarmSystem",
    5001 to "InovaDetector",
    5002 to "InovaSiren",
    5003 to "InovaCommand",
    5004 to "InovaTransmitter",
    5005 to "InovaReciever",
    5006 to "InovaKoala",
    5007 to "InovaInternalTransmitter",
    5008 to "InovaControlPanel",
    5009 to "InovaInputOutputExtension",
    5010 to "InovaMotionDetectorWithVOD",
    5011 to "InovaMotionDetector",
    // Plants (6000 - 6999)
    6000 to "KoubachiPlantSensor"
)
val nodes_protocol = mapOf(
    0 to "None",
    1 to "ZWave",
    2 to "ZigBee",
    3 to "EnOcean",
    4 to "WMBus",
    5 to "Homematic",
    6 to "KNXRF",
    7 to "Inova",
    8 to "HTTPAVM",
    9 to "HTTPNetatmo",
    10 to "HTTPKoubachi",
    11 to "HTTPNest",
    12 to "IOCube",
    100 to "All"
)
val nodes_state = mapOf(
    0 to "None",
    1 to "Available",
    2 to "Unavailable",
    3 to "UpdateInProgress",
    4 to "WaitingForAttributes",
    5 to "Initializing",
    6 to "UserInteractionRequired",
    7 to "PasswordRequired",
    8 to "HostUnavailable",
    9 to "DeleteInProgress",
    10 to "CosiConnected"
)
/** Attributes */
val attributes_type = mapOf(
    0 to "None",
    1 to "OnOff",
    2 to "DimmingLevel",
    3 to "CurrentEnergyUse",
    4 to "AccumulatedEnergyUse",
    5 to "Temperature",
    6 to "TargetTemperature",
    7 to "RelativeHumidity",
    8 to "BatteryLevel",
    9 to "StatusLED",
    10 to "WindowPosition",
    11 to "Brightness",
    12 to "FloodAlarm",
    13 to "Siren",
    14 to "OpenClose",
    15 to "Position",
    16 to "SmokeAlarm",
    17 to "BlackoutAlarm",
    18 to "CurrentValvePosition",
    19 to "BinaryInput",
    20 to "CO2Level",
    21 to "Update",
    22 to "BatteryState",
    23 to "Color",
    24 to "Saturation",
    25 to "MotionAlarm",
    26 to "MotionSensitivity",
    27 to "MotionInsensitivity",
    28 to "MotionAlarmCancelationDelay",
    29 to "WakeUpInterval",
    30 to "TamperAlarm",
    31 to "AcousticSignal",
    32 to "BinaryOutput",
    33 to "LinkQuality",
    34 to "InovaAlarmSystemState",
    35 to "InovaAlarmGroupState",
    36 to "InovaAlarmIntrusionState",
    37 to "InovaAlarmErrorState",
    38 to "InovaAlarmDoorState",
    39 to "InovaAlarmExternalSensor",
    40 to "ButtonState",
    41 to "Hue",
    42 to "ColorTemperature",
    43 to "HardwareRevision",
    44 to "FirmwareRevision",
    45 to "SoftwareRevision",
    46 to "LEDState",
    47 to "LEDStateWhenOn",
    48 to "LEDStateWhenOff",
    49 to "OpenCasingAlarm",
    50 to "AcousticAndVisualSignals",
    51 to "TemperatureMeasurementInterval",
    52 to "HighTemperatureAlarm",
    53 to "HighTemperatureAlarmTreshold",
    54 to "LowTemperatureAlarm",
    55 to "LowTemperatureAlarmTreshold",
    56 to "TamperSensitivity",
    57 to "TamperAlarmCancelationDelay",
    58 to "BrightnessReportInterval",
    59 to "TemperatureReportInterval",
    60 to "MotionAlarmIndicationMode",
    61 to "LEDBrightness",
    62 to "TamperAlarmIndicationMode",
    63 to "SwitchType",
    64 to "TemperatureOffset",
    65 to "AccumulatedWaterUse",
    66 to "AccumulatedWaterUseLastMonth",
    67 to "CurrentDate",
    68 to "LeakAlarm",
    69 to "BatteryLowAlarm",
    70 to "MalfunctionAlarm",
    71 to "LinkQualityAlarm",
    72 to "Mode",
    73 to "CurrentState",
    74 to "TargetState",
    75 to "Calibration",
    76 to "PresenceAlarm",
    77 to "MinimumAlarm",
    78 to "MaximumAlarm",
    79 to "OilAlarm",
    80 to "WaterAlarm",
    81 to "InovaAlarmInhibition",
    82 to "InovaAlarmEjection",
    83 to "InovaAlarmCommercialRef",
    84 to "InovaAlarmSerialNumber",
    85 to "RadiatorThermostatSummerMode",
    86 to "InovaAlarmOperationMode",
    87 to "AutomaticMode",
    88 to "PollingInterval",
    89 to "FeedTemperature",
    90 to "DisplayOrientation",
    91 to "ManualOperation",
    92 to "DeviceTemperature",
    93 to "Sonometer",
    94 to "AirPressure",
    95 to "OutdoorRelativeHumidity",
    96 to "IndoorRelativeHumidity",
    97 to "OutdoorTemperature",
    98 to "IndoorTemperature",
    99 to "InovaAlarmAntimask",
    100 to "InovaAlarmBackupSupply",
    101 to "RainFall",
    102 to "LastUpdateOnServer",
    103 to "InovaAlarmGeneralHomeCommand",
    104 to "InovaAlarmAlert",
    105 to "InovaAlarmSilentAlert",
    106 to "InovaAlarmPreAlarm",
    107 to "InovaAlarmDeterrenceAlarm",
    108 to "InovaAlarmWarning",
    109 to "InovaAlarmFireAlarm",
    110 to "UpTime",
    111 to "DownTime",
    112 to "ShutterBlindMode",
    113 to "ShutterSlatPosition",
    114 to "ShutterSlatTime",
    115 to "RestartDevice",
    116 to "SoilMoisture",
    117 to "WaterPlantAlarm",
    118 to "MistPlantAlarm",
    119 to "FertilizePlantAlarm",
    120 to "CoolPlantAlarm",
    121 to "HeatPlantAlarm",
    122 to "PutPlantIntoLightAlarm",
    123 to "PutPlantIntoShadeAlarm",
    124 to "ColorMode",
    125 to "TargetTemperatureLow",
    126 to "TargetTemperatureHigh",
    127 to "HVACMode",
    128 to "Away",
    129 to "HVACState",
    130 to "HasLeaf",
    131 to "SetEnergyConsumption",
    132 to "COAlarm",
    133 to "RestoreLastKnownState",
    134 to "LastImageReceived",
    135 to "UpDown",
    136 to "RequestVOD",
    137 to "InovaDetectorHistory",
    138 to "SurgeAlarm",
    139 to "LoadAlarm",
    140 to "OverloadAlarm",
    141 to "VolatageDropAlarm",
    142 to "ShutterOrientation",
    143 to "OverCurrentAlarm",
    144 to "SirenMode",
    145 to "AlarmAutoStopTime",
    146 to "WindSpeed",
    147 to "WindDirection",
    205 to "HomeeMode"
)
val attributes_state = mapOf(
    0 to "None",
    1 to "Normal",
    2 to "WaitingForWakeup",
    3 to "WaitingForValue",
    4 to "WaitingForAcknowledge",
    5 to "Inactive"
)
val attributes_changed_by = mapOf(
    0 to "ByNone",
    1 to "ByItself",
    2 to "ByUser",
    3 to "ByHomeegram"
)
val attributes_based_on = mapOf(
    0 to "OnEvents",
    1 to "OnInterval",
    2 to "OnPolling"
)
/** Groups */
val groups_state = mapOf(
    0 to "None",
    1 to "Normal",
    2 to "Executing"
)
val groups_category = mapOf(
    0 to "None"
)
/** Triggers */
val triggers = mapOf(
    0 to "None",
    1 to "Time",
    2 to "Attribute",
    3 to "Switch"
)
val attribute_triggers_operator = mapOf(
    0 to "None",
    1 to "RiseAbove",
    2 to "FallBelow",
    3 to "BecomeEqual",
    4 to "AnyChangeGreaterThan"
)
/** Homeegram Conditions */
val homegram_condition = mapOf(
    0 to "None",
    1 to "Time",
    2 to "Attribute"
)
val attribute_conditions_operator = mapOf(
    0 to "None",
    1 to "Equal",
    2 to "LessEqual",
    3 to "GreaterEqual",
    4 to "LessThan",
    5 to "GreaterThan"
)
val attribute_conditions_check_moment = mapOf(
    0 to "None",
    1 to "Start",
    2 to "End",
    3 to "StartAndEnd"
)
/** Homeegram Actions */
val actions_type = mapOf(
    0 to "None",
    1 to "Attribute",
    2 to "TTS",
    3 to "Notification",
    4 to "Group"
)
val notification_actions_style = mapOf(
    0 to "None",
    1 to "Push",
    2 to "SMS",
    3 to "Email"
)
/** Settings */
val settings_wlan_mode = mapOf(
    0 to "None",
    1 to "Hotspot",
    2 to "Client",
    3 to "Bridge"
)

val cubes_type = mapOf(
    0 to "None",
    1 to "PurpleZWave",
    2 to "OrangeZigBee",
    3 to "CyanEnocean",
    4 to "YellowWMbus",
    5 to "Inova",
    6 to "KNXRF",
    7 to "Homematic",
    8 to "IOCube"
)
/** Warnings */
val warning_code = mapOf(
    // Cube
    100 to "CubeAdded",
    101 to "CubeRemoved",
    102 to "CubeIsMissing",
    103 to "CubeInLearnMode",
    104 to "CubeLearnModeStarted",
    105 to "CubeLearnModeSuccessful",
    106 to "CubeLearnModeTimeout",
    107 to "CubeLearnModeNodeAlreadyAdded",
    108 to "CubeLearnModeFailed",
    109 to "CubeInRemoveMode",
    110 to "CubeRemoveModeStarted",
    111 to "CubeRemoveModeSuccessful",
    112 to "CubeRemoveModeTimeout",
    113 to "CubeRemoveModeNodeAlreadyDeleted",
    114 to "CubeRemoveModeFailed",
    115 to "CubeScannedNodes",
    116 to "CubeUpdateInProgess",
    117 to "CubeUpdateStarted",
    118 to "CubeUpdateEnded",
    119 to "CubeUpdateFailed",
    120 to "CubeUserInteractionRequired",
    121 to "CubeRemoveModeCanceled",
    122 to "CubeLearnModeCanceled",
    123 to "CubeAuthorizationRequired",
    // Node
    200 to "NodeBadLinkQuality",
    201 to "NodeIsMissing",
    202 to "NodeWaterDetected",
    203 to "NodeSmokeDetected",
    204 to "NodeBatteryLow",
    205 to "NodeLocked",
    206 to "NodeNotCompatible",
    207 to "NodeResetSuccessful",
    208 to "NodeResetStarted",
    209 to "NodeResetFailed",
    210 to "NodeResetTimeout",
    211 to "NodeWrongHVACMode",
    212 to "NodeRangeError",
    213 to "NodeBlocked",
    214 to "NodeWrongAwayMode",
    215 to "NodeResetCanceled",
    // Settings
    300 to "SettingRemoteAccessActivated",
    301 to "SettingRemoteAccessDeactivated",
    302 to "SettingOnline",
    303 to "SettingOffline",
    304 to "SettingNetworkUninitialized",
    305 to "SettingNetworkInitializing",
    306 to "SettingNetworkInitialized",
    // Update
    400 to "UpdateAvailable",
    401 to "UpdateDownloading",
    402 to "UpdateInstalling",
    403 to "UpdateInProgress",
    404 to "UpdateSuccessful",
    405 to "UpdateConnectionFailed",
    406 to "UpdateDownloadFailed",
    407 to "UpdateInstallationFailed",
    408 to "UpdatePreparing",
    // Access
    500 to "PermissionDenied",
    501 to "TeachInForbidden",
    502 to "PermissionGranted",
    503 to "VideoCodeWrong",
    // Device
    600 to "DeviceRemoved",
    601 to "DeviceAdded",
    // User
    700 to "UserRemoved",
    701 to "AllUsersRemoved",
    702 to "UserPasswordChangeRequired",
    703 to "UserPasswordChangeFailed",
    704 to "UserPasswordChangeSuccessful",
    // Inova
    800 to "InovaIntrusionDetected",
    801 to "InovaError",
    802 to "InovaDetectorEjected",
    803 to "InovaDoorOpen",
    804 to "InovaWrongOperationMode",
    805 to "InovaCommandTimeout",
    806 to "InovaCommandForbidden",
    807 to "InovaArmingBlocked"
)
/**
 * Mapping: maps node profiles to values (defaults to "current_value")
 */
val nodes_profile_value_mapping = mapOf(
    "None" to "",
    "Homee Cube" to "",
    // Generic (10-999)
    "OnOffPlug" to "on_off",
    "DimmableMeteringSwitch" to "dimming_level",
    "MeteringSwitch" to "on_off",
    "MeteringPlug" to "on_off",
    "DimmablePlug" to "on_off",
    "DimmableSwitch" to "",
    "OnOffSwitch" to "on_off",
    "DoubleOnOffSwitch" to "",
    "DimmableMeteringPlug" to "on_off",
    "OneButtonRemote" to "button_state",
    "BinaryInput" to "",
    "DimmableColorMeteringSwitch" to "",
    "DoubleBinaryInput" to "",
    "TwoButtonRemote" to "button_state",
    "ThreeButtonRemote" to "button_state",
    "FourButtonRemote" to "button_state",
    "AlarmSensor" to "",
    // Lighting (1000 - 1999
    "BrightnessSensor" to "",
    "DimmableColorLight" to "",
    "DimmableExtendedColorLight" to "",
    "DimmableColorTemperatureLight" to "",
    // Closures (2000 - 2999)
    "OpenCloseSensor" to "open_close",
    "WindowHandle" to "open_close",
    "ShutterPositionSwitch" to "open_close",
    "OpenCloseAndTemperatureSensor" to "open_close",
    "ElectricMotorMeteringSwitch" to "open_close",
    "??" to "",
    // HVAC (3000 - 3999)
    "TemperatureAndHumiditySensor" to "target_temperature",
    "CO2Sensor" to "",
    "RoomThermostat" to "target_temperature",
    "RoomThermostatWithHumiditySensor" to "",
    "BinaryInputWithTemperatureSensor" to "target_temperature",
    "RadiatorThermostat" to "target_temperature",
    "TemperatureSensor" to "target_temperature",
    "HumiditySensor" to "",
    "WaterValve" to "",
    "WaterMeter" to "",
    "WeatherStation" to "",
    "NetatmoMainModule" to "",
    "NetatmoOutdoorModule" to "",
    "NetatmoIndoorModule" to "",
    "NetatmoRainModule" to "",
    "TwoChannelCosiTherm" to "",
    "SixChannelCosiTherm" to "",
    "NestThermostatWithCoolinng" to "target_temperature",
    "NestThermostatWithHeating" to "target_temperature",
    "NestThermostatWithHeatingAndCooling" to "target_temperature",
    "NetatmoWindModule" to "",
    // Alarm (4000 - 4999)
    "MotionDetectorWithTemperatureBrightnessAndHumiditySensor" to "",
    "MotionDetector" to "",
    "SmokeDetector" to "",
    "FloodDetector" to "",
    "PresenceDetector" to "",
    "MotionDetectorWithTemperatureAndBrightnessSensor" to "",
    "SmokeDetectorWithTemperatureSensor" to "",
    "FloodDetectorWithTemperatureSensor" to "",
    "WatchDogDevice" to "",
    "LAG" to "",
    "OWU" to "",
    "Eurovac" to "",
    "OWWG3" to "",
    "Europress" to "",
    "MinimumDetector" to "",
    "MaximumDetector" to "",
    "SmokeDetectorAndCODetector" to "",
    // Hager (5000 - 5999)
    "InovaAlarmSystem" to "",
    "InovaDetector" to "",
    "InovaSiren" to "",
    "InovaCommand" to "",
    "InovaTransmitter" to "",
    "InovaReciever" to "",
    "InovaKoala" to "",
    "InovaInternalTransmitter" to "",
    "InovaControlPanel" to "",
    "InovaInputOutputExtension" to "",
    "InovaMotionDetectorWithVOD" to "",
    "InovaMotionDetector" to "",
    // Plants (6000 - 6999)
    "KoubachiPlantSensor" to ""
)
/**
 * Mapping: maps node profiles to values (defaults to "current_value")
 */
val nodes_profile_type_mapping = mapOf(
    "None" to "",
    "Homee Cube" to "",
    // Generic (10-999)
    "OnOffPlug" to "plug",
    "DimmableMeteringSwitch" to "light",
    "MeteringSwitch" to "light",
    "MeteringPlug" to "plug",
    "DimmablePlug" to "plug",
    "DimmableSwitch" to "light",
    "OnOffSwitch" to "remote",
    "DoubleOnOffSwitch" to "remote",
    "DimmableMeteringPlug" to "plug",
    "OneButtonRemote" to "remote",
    "BinaryInput" to "",
    "DimmableColorMeteringSwitch" to "",
    "DoubleBinaryInput" to "",
    "TwoButtonRemote" to "remote",
    "ThreeButtonRemote" to "remote",
    "FourButtonRemote" to "remote",
    "AlarmSensor" to "",
    // Lighting (1000 - 1999
    "BrightnessSensor" to "",
    "DimmableColorLight" to "",
    "DimmableExtendedColorLight" to "",
    "DimmableColorTemperatureLight" to "",
    // Closures (2000 - 2999)
    "OpenCloseSensor" to "state",
    "WindowHandle" to "state",
    "ShutterPositionSwitch" to "state",
    "OpenCloseAndTemperatureSensor" to "state",
    "ElectricMotorMeteringSwitch" to "state",
    "??" to "",
    // HVAC (3000 - 3999)
    "TemperatureAndHumiditySensor" to "",
    "CO2Sensor" to "",
    "RoomThermostat" to "thermostat",
    "RoomThermostatWithHumiditySensor" to "",
    "BinaryInputWithTemperatureSensor" to "thermostat",
    "RadiatorThermostat" to "thermostat",
    "TemperatureSensor" to "thermostat",
    "HumiditySensor" to "",
    "WaterValve" to "",
    "WaterMeter" to "",
    "WeatherStation" to "",
    "NetatmoMainModule" to "",
    "NetatmoOutdoorModule" to "",
    "NetatmoIndoorModule" to "",
    "NetatmoRainModule" to "",
    "TwoChannelCosiTherm" to "",
    "SixChannelCosiTherm" to "",
    "NestThermostatWithCoolinng" to "thermostat",
    "NestThermostatWithHeating" to "thermostat",
    "NestThermostatWithHeatingAndCooling" to "thermostat",
    "NetatmoWindModule" to "",
    // Alarm (4000 - 4999)
    "MotionDetectorWithTemperatureBrightnessAndHumiditySensor" to "",
    "MotionDetector" to "",
    "SmokeDetector" to "",
    "FloodDetector" to "",
    "PresenceDetector" to "",
    "MotionDetectorWithTemperatureAndBrightnessSensor" to "",
    "SmokeDetectorWithTemperatureSensor" to "",
    "FloodDetectorWithTemperatureSensor" to "",
    "WatchDogDevice" to "",
    "LAG" to "",
    "OWU" to "",
    "Eurovac" to "",
    "OWWG3" to "",
    "Europress" to "",
    "MinimumDetector" to "",
    "MaximumDetector" to "",
    "SmokeDetectorAndCODetector" to "",
    // Hager (5000 - 5999)
    "InovaAlarmSystem" to "",
    "InovaDetector" to "",
    "InovaSiren" to "",
    "InovaCommand" to "",
    "InovaTransmitter" to "",
    "InovaReciever" to "",
    "InovaKoala" to "",
    "InovaInternalTransmitter" to "",
    "InovaControlPanel" to "",
    "InovaInputOutputExtension" to "",
    "InovaMotionDetectorWithVOD" to "",
    "InovaMotionDetector" to "",
    // Plants (6000 - 6999)
    "KoubachiPlantSensor" to ""
)

