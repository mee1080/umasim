/*
 * Copyright 2023 mee1080
 *
 * This file is part of umasim.
 *
 * umasim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * umasim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with umasim.  If not, see <https://www.gnu.org/licenses/>.
 */
/*
 * This file was ported from uma-clock-emu by Romulus Urakagi Tsai(@urakagi)
 * https://github.com/urakagi/uma-clock-emu
 */
package io.github.mee1080.umasim.race.data

internal val rawCourseData = """
{
  "10001": {
    "name": "札幌",
    "courses": {
      "10101": {
        "raceTrackId": 10001,
        "name": "芝1200m",
        "distance": 1200,
        "distanceType": 1,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 13500,
        "finishTimeMin": 67.5,
        "finishTimeMax": 71.0,
        "corners": [
          {
            "start": 400.0,
            "length": 275.0
          },
          {
            "start": 675.0,
            "length": 259.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 400.0
          },
          {
            "start": 934.0,
            "end": 1200.0
          }
        ],
        "slopes": []
      },
      "10102": {
        "raceTrackId": 10001,
        "name": "芝1500m",
        "distance": 1500,
        "distanceType": 2,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 13500,
        "finishTimeMin": 87.9,
        "finishTimeMax": 95.0,
        "corners": [
          {
            "start": 150.0,
            "length": 275.0
          },
          {
            "start": 700.0,
            "length": 275.0
          },
          {
            "start": 975.0,
            "length": 259.0
          }
        ],
        "straights": [
          {
            "start": 425.0,
            "end": 700.0
          },
          {
            "start": 1234.0,
            "end": 1500.0
          }
        ],
        "slopes": []
      },
      "10103": {
        "raceTrackId": 10001,
        "name": "芝1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 13500,
        "finishTimeMin": 104.4,
        "finishTimeMax": 110.0,
        "corners": [
          {
            "start": 175.0,
            "length": 275.0
          },
          {
            "start": 450.0,
            "length": 275.0
          },
          {
            "start": 1000.0,
            "length": 275.0
          },
          {
            "start": 1275.0,
            "length": 259.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 175.0
          },
          {
            "start": 725.0,
            "end": 1000.0
          },
          {
            "start": 1534.0,
            "end": 1800.0
          }
        ],
        "slopes": []
      },
      "10104": {
        "raceTrackId": 10001,
        "name": "芝2000m",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 13500,
        "finishTimeMin": 117.1,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 375.0,
            "length": 275.0
          },
          {
            "start": 650.0,
            "length": 275.0
          },
          {
            "start": 1200.0,
            "length": 275.0
          },
          {
            "start": 1475.0,
            "length": 259.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 375.0
          },
          {
            "start": 925.0,
            "end": 1200.0
          },
          {
            "start": 1734.0,
            "end": 2000.0
          }
        ],
        "slopes": []
      },
      "10105": {
        "raceTrackId": 10001,
        "name": "芝2600m",
        "distance": 2600,
        "distanceType": 4,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          2
        ],
        "laneMax": 13500,
        "finishTimeMin": 157.6,
        "finishTimeMax": 165.0,
        "corners": [
          {
            "start": 175.0,
            "length": 275.0
          },
          {
            "start": 450.0,
            "length": 275.0
          },
          {
            "start": 975.0,
            "length": 275.0
          },
          {
            "start": 1250.0,
            "length": 275.0
          },
          {
            "start": 1800.0,
            "length": 275.0
          },
          {
            "start": 2075.0,
            "length": 259.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 175.0
          },
          {
            "start": 725.0,
            "end": 975.0
          },
          {
            "start": 1525.0,
            "end": 1800.0
          },
          {
            "start": 2334.0,
            "end": 2600.0
          }
        ],
        "slopes": []
      },
      "10106": {
        "raceTrackId": 10001,
        "name": "ダート1000m",
        "distance": 1000,
        "distanceType": 1,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 11000,
        "finishTimeMin": 57.4,
        "finishTimeMax": 63.0,
        "corners": [
          {
            "start": 280.0,
            "length": 230.0
          },
          {
            "start": 510.0,
            "length": 226.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 280.0
          },
          {
            "start": 736.0,
            "end": 1000.0
          }
        ],
        "slopes": []
      },
      "10107": {
        "raceTrackId": 10001,
        "name": "ダート1700m",
        "distance": 1700,
        "distanceType": 2,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [
          1
        ],
        "laneMax": 11000,
        "finishTimeMin": 101.4,
        "finishTimeMax": 113.0,
        "corners": [
          {
            "start": 240.0,
            "length": 230.0
          },
          {
            "start": 470.0,
            "length": 230.0
          },
          {
            "start": 980.0,
            "length": 230.0
          },
          {
            "start": 1210.0,
            "length": 226.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 240.0
          },
          {
            "start": 700.0,
            "end": 980.0
          },
          {
            "start": 1436.0,
            "end": 1700.0
          }
        ],
        "slopes": []
      },
      "10108": {
        "raceTrackId": 10001,
        "name": "ダート2400m",
        "distance": 2400,
        "distanceType": 3,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 11000,
        "finishTimeMin": 149.1,
        "finishTimeMax": 156.0,
        "corners": [
          {
            "start": 200.0,
            "length": 230.0
          },
          {
            "start": 430.0,
            "length": 230.0
          },
          {
            "start": 940.0,
            "length": 230.0
          },
          {
            "start": 1170.0,
            "length": 230.0
          },
          {
            "start": 1680.0,
            "length": 230.0
          },
          {
            "start": 1910.0,
            "length": 226.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 200.0
          },
          {
            "start": 660.0,
            "end": 940.0
          },
          {
            "start": 1408.0,
            "end": 1680.0
          },
          {
            "start": 2136.0,
            "end": 2400.0
          }
        ],
        "slopes": []
      }
    }
  },
  "10002": {
    "name": "函館",
    "courses": {
      "10201": {
        "raceTrackId": 10002,
        "name": "芝1000m",
        "distance": 1000,
        "distanceType": 1,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 14700,
        "finishTimeMin": 54.7,
        "finishTimeMax": 57.0,
        "corners": [
          {
            "start": 310.0,
            "length": 220.0
          },
          {
            "start": 530.0,
            "length": 208.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 310.0
          },
          {
            "start": 738.0,
            "end": 1000.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 555.0,
            "slope": 10000.0
          }
        ]
      },
      "10202": {
        "raceTrackId": 10002,
        "name": "芝1200m",
        "distance": 1200,
        "distanceType": 1,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 14700,
        "finishTimeMin": 67.5,
        "finishTimeMax": 71.0,
        "corners": [
          {
            "start": 510.0,
            "length": 220.0
          },
          {
            "start": 730.0,
            "length": 208.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 510.0
          },
          {
            "start": 938.0,
            "end": 1200.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 755.0,
            "slope": 10000.0
          }
        ]
      },
      "10203": {
        "raceTrackId": 10002,
        "name": "芝1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 14700,
        "finishTimeMin": 104.4,
        "finishTimeMax": 110.0,
        "corners": [
          {
            "start": 320.0,
            "length": 220.0
          },
          {
            "start": 540.0,
            "length": 220.0
          },
          {
            "start": 1110.0,
            "length": 220.0
          },
          {
            "start": 1330.0,
            "length": 208.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 320.0
          },
          {
            "start": 760.0,
            "end": 1110.0
          },
          {
            "start": 1538.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 220.0,
            "length": 200.0,
            "slope": -10000.0
          },
          {
            "start": 665.0,
            "length": 720.0,
            "slope": 10000.0
          }
        ]
      },
      "10204": {
        "raceTrackId": 10002,
        "name": "芝2000m",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          1
        ],
        "laneMax": 14700,
        "finishTimeMin": 117.1,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 520.0,
            "length": 220.0
          },
          {
            "start": 740.0,
            "length": 220.0
          },
          {
            "start": 1310.0,
            "length": 220.0
          },
          {
            "start": 1530.0,
            "length": 208.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 520.0
          },
          {
            "start": 960.0,
            "end": 1310.0
          },
          {
            "start": 1738.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 420.0,
            "length": 200.0,
            "slope": -10000.0
          },
          {
            "start": 865.0,
            "length": 720.0,
            "slope": 10000.0
          }
        ]
      },
      "10205": {
        "raceTrackId": 10002,
        "name": "芝2600m",
        "distance": 2600,
        "distanceType": 4,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          2
        ],
        "laneMax": 14700,
        "finishTimeMin": 157.6,
        "finishTimeMax": 165.0,
        "corners": [
          {
            "start": 260.0,
            "length": 240.0
          },
          {
            "start": 500.0,
            "length": 230.0
          },
          {
            "start": 1120.0,
            "length": 220.0
          },
          {
            "start": 1340.0,
            "length": 220.0
          },
          {
            "start": 1910.0,
            "length": 220.0
          },
          {
            "start": 2130.0,
            "length": 208.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 260.0
          },
          {
            "start": 730.0,
            "end": 1120.0
          },
          {
            "start": 1560.0,
            "end": 1910.0
          },
          {
            "start": 2338.0,
            "end": 2600.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 495.0,
            "slope": 10000.0
          },
          {
            "start": 970.0,
            "length": 200.0,
            "slope": -10000.0
          },
          {
            "start": 1425.0,
            "length": 720.0,
            "slope": 10000.0
          }
        ]
      },
      "10206": {
        "raceTrackId": 10002,
        "name": "ダート1000m",
        "distance": 1000,
        "distanceType": 1,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 11000,
        "finishTimeMin": 57.4,
        "finishTimeMax": 63.0,
        "corners": [
          {
            "start": 370.0,
            "length": 190.0
          },
          {
            "start": 560.0,
            "length": 180.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 370.0
          },
          {
            "start": 740.0,
            "end": 1000.0
          }
        ],
        "slopes": []
      },
      "10207": {
        "raceTrackId": 10002,
        "name": "ダート1700m",
        "distance": 1700,
        "distanceType": 2,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 11000,
        "finishTimeMin": 101.4,
        "finishTimeMax": 113.0,
        "corners": [
          {
            "start": 350.0,
            "length": 190.0
          },
          {
            "start": 540.0,
            "length": 190.0
          },
          {
            "start": 1070.0,
            "length": 190.0
          },
          {
            "start": 1260.0,
            "length": 180.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 350.0
          },
          {
            "start": 730.0,
            "end": 1070.0
          },
          {
            "start": 1440.0,
            "end": 1700.0
          }
        ],
        "slopes": [
          {
            "start": 275.0,
            "length": 340.0,
            "slope": -10000.0
          },
          {
            "start": 615.0,
            "length": 670.0,
            "slope": 10000.0
          }
        ]
      },
      "10208": {
        "raceTrackId": 10002,
        "name": "ダート2400m",
        "distance": 2400,
        "distanceType": 3,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [
          2
        ],
        "laneMax": 11000,
        "finishTimeMin": 149.1,
        "finishTimeMax": 156.0,
        "corners": [
          {
            "start": 292.0,
            "length": 190.0
          },
          {
            "start": 482.0,
            "length": 190.0
          },
          {
            "start": 1040.0,
            "length": 190.0
          },
          {
            "start": 1230.0,
            "length": 190.0
          },
          {
            "start": 1770.0,
            "length": 190.0
          },
          {
            "start": 1960.0,
            "length": 180.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 292.0
          },
          {
            "start": 672.0,
            "end": 1040.0
          },
          {
            "start": 1420.0,
            "end": 1770.0
          },
          {
            "start": 2140.0,
            "end": 2400.0
          }
        ],
        "slopes": []
      }
    }
  },
  "10003": {
    "name": "新潟",
    "courses": {
      "10301": {
        "raceTrackId": 10003,
        "name": "芝1000m",
        "distance": 1000,
        "distanceType": 1,
        "surface": 1,
        "turn": 4,
        "courseSetStatus": [
          3
        ],
        "laneMax": 23500,
        "finishTimeMin": 54.7,
        "finishTimeMax": 57.0,
        "corners": [],
        "straights": [
          {
            "start": 0.0,
            "end": 1000.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 240.0,
            "slope": 10000.0
          },
          {
            "start": 240.0,
            "length": 60.0,
            "slope": -10000.0
          }
        ]
      },
      "10302": {
        "raceTrackId": 10003,
        "name": "芝1200m(内)",
        "distance": 1200,
        "distanceType": 1,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 13500,
        "finishTimeMin": 67.5,
        "finishTimeMax": 71.0,
        "corners": [
          {
            "start": 450.0,
            "length": 200.0
          },
          {
            "start": 650.0,
            "length": 192.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 450.0
          },
          {
            "start": 842.0,
            "end": 1200.0
          }
        ],
        "slopes": []
      },
      "10303": {
        "raceTrackId": 10003,
        "name": "芝1400m(内)",
        "distance": 1400,
        "distanceType": 1,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 13500,
        "finishTimeMin": 80.0,
        "finishTimeMax": 84.0,
        "corners": [
          {
            "start": 650.0,
            "length": 200.0
          },
          {
            "start": 850.0,
            "length": 192.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 650.0
          },
          {
            "start": 1042.0,
            "end": 1400.0
          }
        ],
        "slopes": []
      },
      "10304": {
        "raceTrackId": 10003,
        "name": "芝1600m(外)",
        "distance": 1600,
        "distanceType": 2,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 13500,
        "finishTimeMin": 90.8,
        "finishTimeMax": 95.0,
        "corners": [
          {
            "start": 550.0,
            "length": 200.0
          },
          {
            "start": 750.0,
            "length": 192.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 550.0
          },
          {
            "start": 942.0,
            "end": 1600.0
          }
        ],
        "slopes": [
          {
            "start": 250.0,
            "length": 350.0,
            "slope": 10000.0
          },
          {
            "start": 600.0,
            "length": 300.0,
            "slope": -15000.0
          }
        ]
      },
      "10305": {
        "raceTrackId": 10003,
        "name": "芝1800m(外)",
        "distance": 1800,
        "distanceType": 2,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [
          3
        ],
        "laneMax": 13500,
        "finishTimeMin": 104.4,
        "finishTimeMax": 110.0,
        "corners": [
          {
            "start": 750.0,
            "length": 200.0
          },
          {
            "start": 950.0,
            "length": 192.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 750.0
          },
          {
            "start": 1142.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 450.0,
            "length": 350.0,
            "slope": 10000.0
          },
          {
            "start": 800.0,
            "length": 300.0,
            "slope": -15000.0
          }
        ]
      },
      "10306": {
        "raceTrackId": 10003,
        "name": "芝2000m(内)",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [
          2,
          3
        ],
        "laneMax": 13500,
        "finishTimeMin": 117.1,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 420.0,
            "length": 200.0
          },
          {
            "start": 620.0,
            "length": 200.0
          },
          {
            "start": 1250.0,
            "length": 200.0
          },
          {
            "start": 1450.0,
            "length": 192.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 420.0
          },
          {
            "start": 820.0,
            "end": 1250.0
          },
          {
            "start": 1642.0,
            "end": 2000.0
          }
        ],
        "slopes": []
      },
      "10307": {
        "raceTrackId": 10003,
        "name": "芝2000m(外)",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [
          2,
          3
        ],
        "laneMax": 13500,
        "finishTimeMin": 117.1,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 950.0,
            "length": 200.0
          },
          {
            "start": 1150.0,
            "length": 192.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 950.0
          },
          {
            "start": 1342.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 1000.0,
            "length": 300.0,
            "slope": -15000.0
          },
          {
            "start": 650.0,
            "length": 350.0,
            "slope": 10000.0
          }
        ]
      },
      "10308": {
        "raceTrackId": 10003,
        "name": "芝2200m(内)",
        "distance": 2200,
        "distanceType": 3,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [
          1
        ],
        "laneMax": 13500,
        "finishTimeMin": 130.2,
        "finishTimeMax": 135.0,
        "corners": [
          {
            "start": 650.0,
            "length": 200.0
          },
          {
            "start": 850.0,
            "length": 200.0
          },
          {
            "start": 1450.0,
            "length": 200.0
          },
          {
            "start": 1650.0,
            "length": 192.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 650.0
          },
          {
            "start": 1050.0,
            "end": 1450.0
          },
          {
            "start": 1842.0,
            "end": 2200.0
          }
        ],
        "slopes": []
      },
      "10309": {
        "raceTrackId": 10003,
        "name": "芝2400m(内)",
        "distance": 2400,
        "distanceType": 3,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 13500,
        "finishTimeMin": 141.6,
        "finishTimeMax": 149.0,
        "corners": [
          {
            "start": 810.0,
            "length": 200.0
          },
          {
            "start": 1010.0,
            "length": 200.0
          },
          {
            "start": 1650.0,
            "length": 200.0
          },
          {
            "start": 1850.0,
            "length": 192.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 810.0
          },
          {
            "start": 1210.0,
            "end": 1650.0
          },
          {
            "start": 2042.0,
            "end": 2400.0
          }
        ],
        "slopes": []
      },
      "10310": {
        "raceTrackId": 10003,
        "name": "ダート1200m",
        "distance": 1200,
        "distanceType": 1,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 11000,
        "finishTimeMin": 69.0,
        "finishTimeMax": 77.0,
        "corners": [
          {
            "start": 540.0,
            "length": 160.0
          },
          {
            "start": 700.0,
            "length": 147.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 540.0
          },
          {
            "start": 847.0,
            "end": 1200.0
          }
        ],
        "slopes": []
      },
      "10311": {
        "raceTrackId": 10003,
        "name": "ダート1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          5
        ],
        "laneMax": 11000,
        "finishTimeMin": 108.1,
        "finishTimeMax": 118.0,
        "corners": [
          {
            "start": 400.0,
            "length": 160.0
          },
          {
            "start": 560.0,
            "length": 160.0
          },
          {
            "start": 1140.0,
            "length": 160.0
          },
          {
            "start": 1300.0,
            "length": 147.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 400.0
          },
          {
            "start": 720.0,
            "end": 1140.0
          },
          {
            "start": 1447.0,
            "end": 1800.0
          }
        ],
        "slopes": []
      },
      "10312": {
        "raceTrackId": 10003,
        "name": "ダート2500m",
        "distance": 2500,
        "distanceType": 4,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 11000,
        "finishTimeMin": 159.1,
        "finishTimeMax": 164.0,
        "corners": [
          {
            "start": 380.0,
            "length": 160.0
          },
          {
            "start": 540.0,
            "length": 160.0
          },
          {
            "start": 1120.0,
            "length": 160.0
          },
          {
            "start": 1280.0,
            "length": 160.0
          },
          {
            "start": 1850.0,
            "length": 160.0
          },
          {
            "start": 2010.0,
            "length": 160.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 380.0
          },
          {
            "start": 700.0,
            "end": 1120.0
          },
          {
            "start": 1440.0,
            "end": 1850.0
          },
          {
            "start": 2170.0,
            "end": 2500.0
          }
        ],
        "slopes": []
      }
    }
  },
  "10004": {
    "name": "福島",
    "courses": {
      "10401": {
        "raceTrackId": 10004,
        "name": "芝1200m",
        "distance": 1200,
        "distanceType": 1,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 13000,
        "finishTimeMin": 67.5,
        "finishTimeMax": 71.0,
        "corners": [
          {
            "start": 420.0,
            "length": 300.0
          },
          {
            "start": 720.0,
            "length": 188.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 420.0
          },
          {
            "start": 908.0,
            "end": 1200.0
          }
        ],
        "slopes": [
          {
            "start": 180.0,
            "length": 100.0,
            "slope": 15000.0
          }
        ]
      },
      "10402": {
        "raceTrackId": 10004,
        "name": "芝1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          2
        ],
        "laneMax": 13000,
        "finishTimeMin": 104.4,
        "finishTimeMax": 110.0,
        "corners": [
          {
            "start": 330.0,
            "length": 200.0
          },
          {
            "start": 530.0,
            "length": 200.0
          },
          {
            "start": 1020.0,
            "length": 300.0
          },
          {
            "start": 1320.0,
            "length": 188.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 330.0
          },
          {
            "start": 730.0,
            "end": 1020.0
          },
          {
            "start": 1508.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 780.0,
            "length": 100.0,
            "slope": 15000.0
          }
        ]
      },
      "10403": {
        "raceTrackId": 10004,
        "name": "芝2000m",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          2
        ],
        "laneMax": 13000,
        "finishTimeMin": 117.1,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 530.0,
            "length": 200.0
          },
          {
            "start": 730.0,
            "length": 200.0
          },
          {
            "start": 1220.0,
            "length": 300.0
          },
          {
            "start": 1520.0,
            "length": 188.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 530.0
          },
          {
            "start": 930.0,
            "end": 1220.0
          },
          {
            "start": 1708.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 980.0,
            "length": 100.0,
            "slope": 15000.0
          }
        ]
      },
      "10404": {
        "raceTrackId": 10004,
        "name": "芝2600m",
        "distance": 2600,
        "distanceType": 4,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 13000,
        "finishTimeMin": 157.6,
        "finishTimeMax": 165.0,
        "corners": [
          {
            "start": 220.0,
            "length": 300.0
          },
          {
            "start": 520.0,
            "length": 200.0
          },
          {
            "start": 1130.0,
            "length": 200.0
          },
          {
            "start": 1330.0,
            "length": 200.0
          },
          {
            "start": 1820.0,
            "length": 300.0
          },
          {
            "start": 2120.0,
            "length": 188.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 220.0
          },
          {
            "start": 720.0,
            "end": 1130.0
          },
          {
            "start": 1530.0,
            "end": 1820.0
          },
          {
            "start": 2308.0,
            "end": 2600.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 80.0,
            "slope": 15000.0
          },
          {
            "start": 1580.0,
            "length": 100.0,
            "slope": 15000.0
          }
        ]
      },
      "10405": {
        "raceTrackId": 10004,
        "name": "ダート1150m",
        "distance": 1150,
        "distanceType": 1,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 11500,
        "finishTimeMin": 66.6,
        "finishTimeMax": 72.0,
        "corners": [
          {
            "start": 500.0,
            "length": 210.0
          },
          {
            "start": 710.0,
            "length": 145.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 500.0
          },
          {
            "start": 855.0,
            "end": 1150.0
          }
        ],
        "slopes": []
      },
      "10406": {
        "raceTrackId": 10004,
        "name": "ダート1700m",
        "distance": 1700,
        "distanceType": 2,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 11500,
        "finishTimeMin": 101.4,
        "finishTimeMax": 113.0,
        "corners": [
          {
            "start": 360.0,
            "length": 170.0
          },
          {
            "start": 530.0,
            "length": 170.0
          },
          {
            "start": 1050.0,
            "length": 210.0
          },
          {
            "start": 1260.0,
            "length": 145.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 360.0
          },
          {
            "start": 700.0,
            "end": 1050.0
          },
          {
            "start": 1405.0,
            "end": 1700.0
          }
        ],
        "slopes": [
          {
            "start": 285.0,
            "length": 320.0,
            "slope": -10000.0
          }
        ]
      },
      "10407": {
        "raceTrackId": 10004,
        "name": "ダート2400m",
        "distance": 2400,
        "distanceType": 3,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [
          2
        ],
        "laneMax": 11500,
        "finishTimeMin": 149.1,
        "finishTimeMax": 156.0,
        "corners": [
          {
            "start": 310.0,
            "length": 210.0
          },
          {
            "start": 520.0,
            "length": 160.0
          },
          {
            "start": 1060.0,
            "length": 170.0
          },
          {
            "start": 1230.0,
            "length": 170.0
          },
          {
            "start": 1750.0,
            "length": 210.0
          },
          {
            "start": 1960.0,
            "length": 145.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 310.0
          },
          {
            "start": 680.0,
            "end": 1060.0
          },
          {
            "start": 1400.0,
            "end": 1750.0
          },
          {
            "start": 2105.0,
            "end": 2400.0
          }
        ],
        "slopes": []
      }
    }
  },
  "10005": {
    "name": "中山",
    "courses": {
      "10501": {
        "raceTrackId": 10005,
        "name": "芝1200m(外)",
        "distance": 1200,
        "distanceType": 1,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 12000,
        "finishTimeMin": 67.5,
        "finishTimeMax": 71.0,
        "corners": [
          {
            "start": 300.0,
            "length": 350.0
          },
          {
            "start": 650.0,
            "length": 240.0
          }
        ],
        "straights": [
          {
            "start": 890.0,
            "end": 1200.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 200.0,
            "slope": -15000.0
          },
          {
            "start": 1025.0,
            "length": 110.0,
            "slope": 20000.0
          }
        ]
      },
      "10502": {
        "raceTrackId": 10005,
        "name": "芝1600m(外)",
        "distance": 1600,
        "distanceType": 2,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 12000,
        "finishTimeMin": 90.8,
        "finishTimeMax": 95.0,
        "corners": [
          {
            "start": 50.0,
            "length": 450.0
          },
          {
            "start": 700.0,
            "length": 350.0
          },
          {
            "start": 1050.0,
            "length": 240.0
          }
        ],
        "straights": [
          {
            "start": 1290.0,
            "end": 1600.0
          }
        ],
        "slopes": [
          {
            "start": 300.0,
            "length": 300.0,
            "slope": -15000.0
          },
          {
            "start": 1425.0,
            "length": 110.0,
            "slope": 20000.0
          }
        ]
      },
      "10503": {
        "raceTrackId": 10005,
        "name": "芝1800m(内)",
        "distance": 1800,
        "distanceType": 2,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 12000,
        "finishTimeMin": 104.4,
        "finishTimeMax": 110.0,
        "corners": [
          {
            "start": 175.0,
            "length": 250.0
          },
          {
            "start": 425.0,
            "length": 250.0
          },
          {
            "start": 1000.0,
            "length": 250.0
          },
          {
            "start": 1250.0,
            "length": 240.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 175.0
          },
          {
            "start": 675.0,
            "end": 1000.0
          },
          {
            "start": 1490.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 1.0,
            "length": 35.0,
            "slope": 20000.0
          },
          {
            "start": 125.0,
            "length": 200.0,
            "slope": 15000.0
          },
          {
            "start": 425.0,
            "length": 400.0,
            "slope": -15000.0
          },
          {
            "start": 1625.0,
            "length": 110.0,
            "slope": 20000.0
          }
        ]
      },
      "10504": {
        "raceTrackId": 10005,
        "name": "芝2000m(内)",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          1
        ],
        "laneMax": 12000,
        "finishTimeMin": 117.1,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 375.0,
            "length": 250.0
          },
          {
            "start": 625.0,
            "length": 250.0
          },
          {
            "start": 1200.0,
            "length": 250.0
          },
          {
            "start": 1450.0,
            "length": 240.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 375.0
          },
          {
            "start": 875.0,
            "end": 1200.0
          },
          {
            "start": 1690.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 325.0,
            "length": 200.0,
            "slope": 15000.0
          },
          {
            "start": 125.0,
            "length": 110.0,
            "slope": 20000.0
          },
          {
            "start": 625.0,
            "length": 400.0,
            "slope": -15000.0
          },
          {
            "start": 1825.0,
            "length": 110.0,
            "slope": 20000.0
          }
        ]
      },
      "10505": {
        "raceTrackId": 10005,
        "name": "芝2200m(外)",
        "distance": 2200,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          2,
          4
        ],
        "laneMax": 12000,
        "finishTimeMin": 130.2,
        "finishTimeMax": 135.0,
        "corners": [
          {
            "start": 403.0,
            "length": 247.0
          },
          {
            "start": 650.0,
            "length": 450.0
          },
          {
            "start": 1300.0,
            "length": 350.0
          },
          {
            "start": 1650.0,
            "length": 240.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 403.0
          },
          {
            "start": 1890.0,
            "end": 2200.0
          }
        ],
        "slopes": [
          {
            "start": 153.0,
            "length": 110.0,
            "slope": 20000.0
          },
          {
            "start": 353.0,
            "length": 200.0,
            "slope": 15000.0
          },
          {
            "start": 900.0,
            "length": 300.0,
            "slope": -15000.0
          },
          {
            "start": 2025.0,
            "length": 110.0,
            "slope": 20000.0
          }
        ]
      },
      "10506": {
        "raceTrackId": 10005,
        "name": "芝2500m(内)",
        "distance": 2500,
        "distanceType": 4,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          2,
          4
        ],
        "laneMax": 12000,
        "finishTimeMin": 148.7,
        "finishTimeMax": 157.0,
        "corners": [
          {
            "start": 100.0,
            "length": 146.0
          },
          {
            "start": 246.0,
            "length": 250.0
          },
          {
            "start": 875.0,
            "length": 250.0
          },
          {
            "start": 1125.0,
            "length": 250.0
          },
          {
            "start": 1700.0,
            "length": 250.0
          },
          {
            "start": 1950.0,
            "length": 240.0
          }
        ],
        "straights": [
          {
            "start": 496.0,
            "end": 875.0
          },
          {
            "start": 1375.0,
            "end": 1700.0
          },
          {
            "start": 2190.0,
            "end": 2500.0
          }
        ],
        "slopes": [
          {
            "start": 621.0,
            "length": 110.0,
            "slope": 20000.0
          },
          {
            "start": 825.0,
            "length": 200.0,
            "slope": 15000.0
          },
          {
            "start": 1125.0,
            "length": 400.0,
            "slope": -15000.0
          },
          {
            "start": 2325.0,
            "length": 110.0,
            "slope": 20000.0
          }
        ]
      },
      "10507": {
        "raceTrackId": 10005,
        "name": "芝3600m(内)",
        "distance": 3600,
        "distanceType": 4,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          2
        ],
        "laneMax": 12000,
        "finishTimeMin": 223.1,
        "finishTimeMax": 227.0,
        "corners": [
          {
            "start": 290.0,
            "length": 250.0
          },
          {
            "start": 540.0,
            "length": 250.0
          },
          {
            "start": 1115.0,
            "length": 250.0
          },
          {
            "start": 1365.0,
            "length": 250.0
          },
          {
            "start": 1975.0,
            "length": 250.0
          },
          {
            "start": 2225.0,
            "length": 250.0
          },
          {
            "start": 2800.0,
            "length": 250.0
          },
          {
            "start": 3050.0,
            "length": 240.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 290.0
          },
          {
            "start": 790.0,
            "end": 1115.0
          },
          {
            "start": 1615.0,
            "end": 1975.0
          },
          {
            "start": 2475.0,
            "end": 2800.0
          },
          {
            "start": 3290.0,
            "end": 3600.0
          }
        ],
        "slopes": [
          {
            "start": 40.0,
            "length": 110.0,
            "slope": 20000.0
          },
          {
            "start": 240.0,
            "length": 200.0,
            "slope": 15000.0
          },
          {
            "start": 540.0,
            "length": 400.0,
            "slope": -15000.0
          },
          {
            "start": 1740.0,
            "length": 110.0,
            "slope": 20000.0
          },
          {
            "start": 1925.0,
            "length": 200.0,
            "slope": 15000.0
          },
          {
            "start": 2225.0,
            "length": 400.0,
            "slope": -15000.0
          },
          {
            "start": 3425.0,
            "length": 110.0,
            "slope": 20000.0
          }
        ]
      },
      "10508": {
        "raceTrackId": 10005,
        "name": "ダート1200m",
        "distance": 1200,
        "distanceType": 1,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 12000,
        "finishTimeMin": 69.0,
        "finishTimeMax": 77.0,
        "corners": [
          {
            "start": 500.0,
            "length": 200.0
          },
          {
            "start": 700.0,
            "length": 192.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 500.0
          },
          {
            "start": 892.0,
            "end": 1200.0
          }
        ],
        "slopes": [
          {
            "start": 175.0,
            "length": 175.0,
            "slope": -15000.0
          },
          {
            "start": 1000.0,
            "length": 175.0,
            "slope": 15000.0
          }
        ]
      },
      "10509": {
        "raceTrackId": 10005,
        "name": "ダート1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 12000,
        "finishTimeMin": 108.1,
        "finishTimeMax": 118.0,
        "corners": [
          {
            "start": 350.0,
            "length": 200.0
          },
          {
            "start": 550.0,
            "length": 200.0
          },
          {
            "start": 1100.0,
            "length": 200.0
          },
          {
            "start": 1300.0,
            "length": 192.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 350.0
          },
          {
            "start": 750.0,
            "end": 1100.0
          },
          {
            "start": 1492.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 100.0,
            "length": 175.0,
            "slope": 15000.0
          },
          {
            "start": 350.0,
            "length": 175.0,
            "slope": 10000.0
          },
          {
            "start": 775.0,
            "length": 175.0,
            "slope": -15000.0
          },
          {
            "start": 1600.0,
            "length": 175.0,
            "slope": 15000.0
          }
        ]
      },
      "10510": {
        "raceTrackId": 10005,
        "name": "ダート2400m",
        "distance": 2400,
        "distanceType": 3,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [
          2
        ],
        "laneMax": 12000,
        "finishTimeMin": 149.1,
        "finishTimeMax": 156.0,
        "corners": [
          {
            "start": 200.0,
            "length": 200.0
          },
          {
            "start": 400.0,
            "length": 200.0
          },
          {
            "start": 950.0,
            "length": 200.0
          },
          {
            "start": 1150.0,
            "length": 200.0
          },
          {
            "start": 1700.0,
            "length": 200.0
          },
          {
            "start": 1900.0,
            "length": 192.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 200.0
          },
          {
            "start": 600.0,
            "end": 950.0
          },
          {
            "start": 1350.0,
            "end": 1700.0
          },
          {
            "start": 2092.0,
            "end": 2400.0
          }
        ],
        "slopes": []
      },
      "10511": {
        "raceTrackId": 10005,
        "name": "ダート2500m",
        "distance": 2500,
        "distanceType": 4,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 12000,
        "finishTimeMin": 159.1,
        "finishTimeMax": 164.0,
        "corners": [
          {
            "start": 300.0,
            "length": 200.0
          },
          {
            "start": 500.0,
            "length": 200.0
          },
          {
            "start": 1050.0,
            "length": 200.0
          },
          {
            "start": 1250.0,
            "length": 200.0
          },
          {
            "start": 1800.0,
            "length": 200.0
          },
          {
            "start": 2000.0,
            "length": 192.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 300.0
          },
          {
            "start": 700.0,
            "end": 1050.0
          },
          {
            "start": 1450.0,
            "end": 1800.0
          },
          {
            "start": 2192.0,
            "end": 2500.0
          }
        ],
        "slopes": []
      }
    }
  },
  "10006": {
    "name": "東京",
    "courses": {
      "10601": {
        "raceTrackId": 10006,
        "name": "芝1400m",
        "distance": 1400,
        "distanceType": 1,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [
          2,
          3
        ],
        "laneMax": 15000,
        "finishTimeMin": 80.0,
        "finishTimeMax": 84.0,
        "corners": [
          {
            "start": 350.0,
            "length": 275.0
          },
          {
            "start": 625.0,
            "length": 250.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 350.0
          },
          {
            "start": 875.0,
            "end": 1400.0
          }
        ],
        "slopes": [
          {
            "start": 125.0,
            "length": 75.0,
            "slope": 20000.0
          },
          {
            "start": 250.0,
            "length": 250.0,
            "slope": -15000.0
          },
          {
            "start": 950.0,
            "length": 150.0,
            "slope": 15000.0
          }
        ]
      },
      "10602": {
        "raceTrackId": 10006,
        "name": "芝1600m",
        "distance": 1600,
        "distanceType": 2,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [
          2,
          4
        ],
        "laneMax": 15000,
        "finishTimeMin": 90.8,
        "finishTimeMax": 95.0,
        "corners": [
          {
            "start": 550.0,
            "length": 275.0
          },
          {
            "start": 825.0,
            "length": 250.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 550.0
          },
          {
            "start": 1075.0,
            "end": 1600.0
          }
        ],
        "slopes": [
          {
            "start": 450.0,
            "length": 250.0,
            "slope": -15000.0
          },
          {
            "start": 325.0,
            "length": 75.0,
            "slope": 20000.0
          },
          {
            "start": 1150.0,
            "length": 150.0,
            "slope": 15000.0
          }
        ]
      },
      "10603": {
        "raceTrackId": 10006,
        "name": "芝1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [
          1
        ],
        "laneMax": 15000,
        "finishTimeMin": 104.4,
        "finishTimeMax": 110.0,
        "corners": [
          {
            "start": 75.0,
            "length": 250.0
          },
          {
            "start": 750.0,
            "length": 275.0
          },
          {
            "start": 1025.0,
            "length": 250.0
          }
        ],
        "straights": [
          {
            "start": 325.0,
            "end": 750.0
          },
          {
            "start": 1275.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 525.0,
            "length": 75.0,
            "slope": 20000.0
          },
          {
            "start": 650.0,
            "length": 250.0,
            "slope": -15000.0
          },
          {
            "start": 1350.0,
            "length": 150.0,
            "slope": 15000.0
          }
        ]
      },
      "10604": {
        "raceTrackId": 10006,
        "name": "芝2000m",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 15000,
        "finishTimeMin": 117.1,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 200.0,
            "length": 200.0
          },
          {
            "start": 950.0,
            "length": 275.0
          },
          {
            "start": 1225.0,
            "length": 250.0
          }
        ],
        "straights": [
          {
            "start": 400.0,
            "end": 950.0
          },
          {
            "start": 1475.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 725.0,
            "length": 75.0,
            "slope": 20000.0
          },
          {
            "start": 850.0,
            "length": 250.0,
            "slope": -15000.0
          },
          {
            "start": 1550.0,
            "length": 150.0,
            "slope": 15000.0
          }
        ]
      },
      "10605": {
        "raceTrackId": 10006,
        "name": "芝2300m",
        "distance": 2300,
        "distanceType": 3,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [
          3
        ],
        "laneMax": 15000,
        "finishTimeMin": 139.3,
        "finishTimeMax": 143.0,
        "corners": [
          {
            "start": 225.0,
            "length": 250.0
          },
          {
            "start": 475.0,
            "length": 325.0
          },
          {
            "start": 1250.0,
            "length": 275.0
          },
          {
            "start": 1525.0,
            "length": 250.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 225.0
          },
          {
            "start": 800.0,
            "end": 1250.0
          },
          {
            "start": 1775.0,
            "end": 2300.0
          }
        ],
        "slopes": [
          {
            "start": 1025.0,
            "length": 75.0,
            "slope": 20000.0
          },
          {
            "start": 1150.0,
            "length": 250.0,
            "slope": -15000.0
          },
          {
            "start": 1850.0,
            "length": 150.0,
            "slope": 15000.0
          }
        ]
      },
      "10606": {
        "raceTrackId": 10006,
        "name": "芝2400m",
        "distance": 2400,
        "distanceType": 3,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 15000,
        "finishTimeMin": 141.6,
        "finishTimeMax": 149.0,
        "corners": [
          {
            "start": 325.0,
            "length": 250.0
          },
          {
            "start": 575.0,
            "length": 325.0
          },
          {
            "start": 1350.0,
            "length": 275.0
          },
          {
            "start": 1625.0,
            "length": 250.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 325.0
          },
          {
            "start": 900.0,
            "end": 1350.0
          },
          {
            "start": 1875.0,
            "end": 2400.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 40.0,
            "slope": 15000.0
          },
          {
            "start": 1125.0,
            "length": 75.0,
            "slope": 20000.0
          },
          {
            "start": 1250.0,
            "length": 250.0,
            "slope": -15000.0
          },
          {
            "start": 1950.0,
            "length": 150.0,
            "slope": 15000.0
          }
        ]
      },
      "10607": {
        "raceTrackId": 10006,
        "name": "芝2500m",
        "distance": 2500,
        "distanceType": 4,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [
          2
        ],
        "laneMax": 15000,
        "finishTimeMin": 148.7,
        "finishTimeMax": 157.0,
        "corners": [
          {
            "start": 425.0,
            "length": 250.0
          },
          {
            "start": 675.0,
            "length": 325.0
          },
          {
            "start": 1450.0,
            "length": 275.0
          },
          {
            "start": 1725.0,
            "length": 250.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 425.0
          },
          {
            "start": 1000.0,
            "end": 1450.0
          },
          {
            "start": 1975.0,
            "end": 2500.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 140.0,
            "slope": 15000.0
          },
          {
            "start": 1225.0,
            "length": 75.0,
            "slope": 20000.0
          },
          {
            "start": 1350.0,
            "length": 250.0,
            "slope": -15000.0
          },
          {
            "start": 2050.0,
            "length": 150.0,
            "slope": 15000.0
          }
        ]
      },
      "10608": {
        "raceTrackId": 10006,
        "name": "芝3400m",
        "distance": 3400,
        "distanceType": 4,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 15000,
        "finishTimeMin": 209.9,
        "finishTimeMax": 214.0,
        "corners": [
          {
            "start": 292.0,
            "length": 275.0
          },
          {
            "start": 567.0,
            "length": 275.0
          },
          {
            "start": 1325.0,
            "length": 250.0
          },
          {
            "start": 1575.0,
            "length": 325.0
          },
          {
            "start": 2350.0,
            "length": 275.0
          },
          {
            "start": 2625.0,
            "length": 250.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 292.0
          },
          {
            "start": 842.0,
            "end": 1325.0
          },
          {
            "start": 1900.0,
            "end": 2350.0
          },
          {
            "start": 2875.0,
            "end": 3400.0
          }
        ],
        "slopes": [
          {
            "start": 67.0,
            "length": 75.0,
            "slope": 20000.0
          },
          {
            "start": 192.0,
            "length": 250.0,
            "slope": -15000.0
          },
          {
            "start": 892.0,
            "length": 150.0,
            "slope": 15000.0
          },
          {
            "start": 2125.0,
            "length": 75.0,
            "slope": 20000.0
          },
          {
            "start": 2250.0,
            "length": 250.0,
            "slope": -15000.0
          },
          {
            "start": 2950.0,
            "length": 150.0,
            "slope": 15000.0
          }
        ]
      },
      "10609": {
        "raceTrackId": 10006,
        "name": "ダート1300m",
        "distance": 1300,
        "distanceType": 1,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          1
        ],
        "laneMax": 12500,
        "finishTimeMin": 76.6,
        "finishTimeMax": 82.0,
        "corners": [
          {
            "start": 350.0,
            "length": 225.0
          },
          {
            "start": 575.0,
            "length": 224.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 350.0
          },
          {
            "start": 799.0,
            "end": 1300.0
          }
        ],
        "slopes": [
          {
            "start": 275.0,
            "length": 200.0,
            "slope": -10000.0
          },
          {
            "start": 800.0,
            "length": 250.0,
            "slope": 15000.0
          }
        ]
      },
      "10610": {
        "raceTrackId": 10006,
        "name": "ダート1400m",
        "distance": 1400,
        "distanceType": 1,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          2
        ],
        "laneMax": 12500,
        "finishTimeMin": 80.8,
        "finishTimeMax": 94.0,
        "corners": [
          {
            "start": 450.0,
            "length": 225.0
          },
          {
            "start": 675.0,
            "length": 224.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 450.0
          },
          {
            "start": 899.0,
            "end": 1400.0
          }
        ],
        "slopes": [
          {
            "start": 375.0,
            "length": 200.0,
            "slope": -10000.0
          },
          {
            "start": 900.0,
            "length": 250.0,
            "slope": 15000.0
          }
        ]
      },
      "10611": {
        "raceTrackId": 10006,
        "name": "ダート1600m",
        "distance": 1600,
        "distanceType": 2,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          1,
          2
        ],
        "laneMax": 12500,
        "finishTimeMin": 94.1,
        "finishTimeMax": 108.0,
        "corners": [
          {
            "start": 650.0,
            "length": 225.0
          },
          {
            "start": 875.0,
            "length": 224.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 650.0
          },
          {
            "start": 1099.0,
            "end": 1600.0
          }
        ],
        "slopes": [
          {
            "start": 575.0,
            "length": 200.0,
            "slope": -10000.0
          },
          {
            "start": 1100.0,
            "length": 250.0,
            "slope": 15000.0
          }
        ]
      },
      "10612": {
        "raceTrackId": 10006,
        "name": "ダート2100m",
        "distance": 2100,
        "distanceType": 3,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 12500,
        "finishTimeMin": 127.4,
        "finishTimeMax": 133.0,
        "corners": [
          {
            "start": 200.0,
            "length": 250.0
          },
          {
            "start": 450.0,
            "length": 250.0
          },
          {
            "start": 1150.0,
            "length": 225.0
          },
          {
            "start": 1375.0,
            "length": 224.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 200.0
          },
          {
            "start": 700.0,
            "end": 1150.0
          },
          {
            "start": 1599.0,
            "end": 2100.0
          }
        ],
        "slopes": [
          {
            "start": 1075.0,
            "length": 200.0,
            "slope": -10000.0
          },
          {
            "start": 1600.0,
            "length": 250.0,
            "slope": 15000.0
          }
        ]
      },
      "10613": {
        "raceTrackId": 10006,
        "name": "ダート2400m",
        "distance": 2400,
        "distanceType": 3,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          2
        ],
        "laneMax": 12500,
        "finishTimeMin": 149.1,
        "finishTimeMax": 156.0,
        "corners": [
          {
            "start": 500.0,
            "length": 250.0
          },
          {
            "start": 750.0,
            "length": 250.0
          },
          {
            "start": 1450.0,
            "length": 225.0
          },
          {
            "start": 1675.0,
            "length": 225.0
          },
          {
            "start": 2348.0,
            "length": 500.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 500.0
          },
          {
            "start": 1000.0,
            "end": 1450.0
          },
          {
            "start": 1900.0,
            "end": 2400.0
          }
        ],
        "slopes": []
      }
    }
  },
  "10007": {
    "name": "中京",
    "courses": {
      "10701": {
        "raceTrackId": 10007,
        "name": "芝1200m",
        "distance": 1200,
        "distanceType": 1,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 14400,
        "finishTimeMin": 67.5,
        "finishTimeMax": 71.0,
        "corners": [
          {
            "start": 300.0,
            "length": 250.0
          },
          {
            "start": 550.0,
            "length": 238.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 300.0
          },
          {
            "start": 788.0,
            "end": 1200.0
          }
        ],
        "slopes": [
          {
            "start": 100.0,
            "length": 775.0,
            "slope": -10000.0
          },
          {
            "start": 875.0,
            "length": 100.0,
            "slope": 20000.0
          }
        ]
      },
      "10702": {
        "raceTrackId": 10007,
        "name": "芝1400m",
        "distance": 1400,
        "distanceType": 1,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 14400,
        "finishTimeMin": 80.0,
        "finishTimeMax": 84.0,
        "corners": [
          {
            "start": 500.0,
            "length": 250.0
          },
          {
            "start": 750.0,
            "length": 238.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 500.0
          },
          {
            "start": 988.0,
            "end": 1400.0
          }
        ],
        "slopes": [
          {
            "start": 300.0,
            "length": 775.0,
            "slope": -10000.0
          },
          {
            "start": 1075.0,
            "length": 100.0,
            "slope": 20000.0
          }
        ]
      },
      "10703": {
        "raceTrackId": 10007,
        "name": "芝1600m",
        "distance": 1600,
        "distanceType": 2,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [
          1
        ],
        "laneMax": 14400,
        "finishTimeMin": 90.8,
        "finishTimeMax": 95.0,
        "corners": [
          {
            "start": 150.0,
            "length": 150.0
          },
          {
            "start": 700.0,
            "length": 250.0
          },
          {
            "start": 950.0,
            "length": 238.0
          }
        ],
        "straights": [
          {
            "start": 300.0,
            "end": 700.0
          },
          {
            "start": 1188.0,
            "end": 1600.0
          }
        ],
        "slopes": [
          {
            "start": 500.0,
            "length": 775.0,
            "slope": -10000.0
          },
          {
            "start": 1275.0,
            "length": 100.0,
            "slope": 20000.0
          }
        ]
      },
      "10704": {
        "raceTrackId": 10007,
        "name": "芝2000m",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 14400,
        "finishTimeMin": 117.1,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 300.0,
            "length": 200.0
          },
          {
            "start": 500.0,
            "length": 200.0
          },
          {
            "start": 1100.0,
            "length": 250.0
          },
          {
            "start": 1350.0,
            "length": 238.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 300.0
          },
          {
            "start": 700.0,
            "end": 1100.0
          },
          {
            "start": 1588.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 50.0,
            "slope": 20000.0
          },
          {
            "start": 900.0,
            "length": 775.0,
            "slope": -10000.0
          },
          {
            "start": 1675.0,
            "length": 100.0,
            "slope": 20000.0
          }
        ]
      },
      "10705": {
        "raceTrackId": 10007,
        "name": "芝2200m",
        "distance": 2200,
        "distanceType": 3,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [
          2
        ],
        "laneMax": 14400,
        "finishTimeMin": 130.2,
        "finishTimeMax": 135.0,
        "corners": [
          {
            "start": 500.0,
            "length": 200.0
          },
          {
            "start": 700.0,
            "length": 200.0
          },
          {
            "start": 1300.0,
            "length": 250.0
          },
          {
            "start": 1550.0,
            "length": 238.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 500.0
          },
          {
            "start": 900.0,
            "end": 1300.0
          },
          {
            "start": 1788.0,
            "end": 2200.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 150.0,
            "slope": -10000.0
          },
          {
            "start": 150.0,
            "length": 100.0,
            "slope": 20000.0
          },
          {
            "start": 1100.0,
            "length": 775.0,
            "slope": -10000.0
          },
          {
            "start": 1875.0,
            "length": 100.0,
            "slope": 20000.0
          }
        ]
      },
      "10706": {
        "raceTrackId": 10007,
        "name": "ダート1200m",
        "distance": 1200,
        "distanceType": 1,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 13000,
        "finishTimeMin": 69.0,
        "finishTimeMax": 77.0,
        "corners": [
          {
            "start": 400.0,
            "length": 200.0
          },
          {
            "start": 600.0,
            "length": 190.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 400.0
          },
          {
            "start": 790.0,
            "end": 1200.0
          }
        ],
        "slopes": []
      },
      "10707": {
        "raceTrackId": 10007,
        "name": "ダート1400m",
        "distance": 1400,
        "distanceType": 1,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 13000,
        "finishTimeMin": 80.8,
        "finishTimeMax": 94.0,
        "corners": [
          {
            "start": 600.0,
            "length": 200.0
          },
          {
            "start": 800.0,
            "length": 190.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 600.0
          },
          {
            "start": 990.0,
            "end": 1400.0
          }
        ],
        "slopes": [
          {
            "start": 425.0,
            "length": 600.0,
            "slope": -15000.0
          },
          {
            "start": 1025.0,
            "length": 150.0,
            "slope": 15000.0
          }
        ]
      },
      "10708": {
        "raceTrackId": 10007,
        "name": "ダート1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          2
        ],
        "laneMax": 13000,
        "finishTimeMin": 108.1,
        "finishTimeMax": 118.0,
        "corners": [
          {
            "start": 270.0,
            "length": 165.0
          },
          {
            "start": 435.0,
            "length": 165.0
          },
          {
            "start": 1000.0,
            "length": 200.0
          },
          {
            "start": 1200.0,
            "length": 190.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 270.0
          },
          {
            "start": 600.0,
            "end": 1000.0
          },
          {
            "start": 1390.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 50.0,
            "slope": 15000.0
          },
          {
            "start": 825.0,
            "length": 600.0,
            "slope": -15000.0
          },
          {
            "start": 1425.0,
            "length": 150.0,
            "slope": 15000.0
          }
        ]
      },
      "10709": {
        "raceTrackId": 10007,
        "name": "ダート1900m",
        "distance": 1900,
        "distanceType": 3,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 13000,
        "finishTimeMin": 114.2,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 370.0,
            "length": 165.0
          },
          {
            "start": 535.0,
            "length": 165.0
          },
          {
            "start": 1100.0,
            "length": 200.0
          },
          {
            "start": 1300.0,
            "length": 190.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 370.0
          },
          {
            "start": 700.0,
            "end": 1100.0
          },
          {
            "start": 1490.0,
            "end": 1900.0
          }
        ],
        "slopes": []
      }
    }
  },
  "10008": {
    "name": "京都",
    "courses": {
      "10801": {
        "raceTrackId": 10008,
        "name": "芝1200m(内)",
        "distance": 1200,
        "distanceType": 1,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 14100,
        "finishTimeMin": 67.5,
        "finishTimeMax": 71.0,
        "corners": [
          {
            "start": 320.0,
            "length": 275.0
          },
          {
            "start": 595.0,
            "length": 277.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 320.0
          },
          {
            "start": 872.0,
            "end": 1200.0
          }
        ],
        "slopes": [
          {
            "start": 120.0,
            "length": 175.0,
            "slope": 15000.0
          },
          {
            "start": 420.0,
            "length": 150.0,
            "slope": -15000.0
          }
        ]
      },
      "10802": {
        "raceTrackId": 10008,
        "name": "芝1400m(内)",
        "distance": 1400,
        "distanceType": 1,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 14100,
        "finishTimeMin": 80.0,
        "finishTimeMax": 84.0,
        "corners": [
          {
            "start": 520.0,
            "length": 275.0
          },
          {
            "start": 795.0,
            "length": 277.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 520.0
          },
          {
            "start": 1072.0,
            "end": 1400.0
          }
        ],
        "slopes": [
          {
            "start": 320.0,
            "length": 175.0,
            "slope": 15000.0
          },
          {
            "start": 620.0,
            "length": 150.0,
            "slope": -15000.0
          }
        ]
      },
      "10803": {
        "raceTrackId": 10008,
        "name": "芝1400m(外)",
        "distance": 1400,
        "distanceType": 1,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 14100,
        "finishTimeMin": 80.0,
        "finishTimeMax": 84.0,
        "corners": [
          {
            "start": 500.0,
            "length": 250.0
          },
          {
            "start": 750.0,
            "length": 247.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 500.0
          },
          {
            "start": 997.0,
            "end": 1400.0
          }
        ],
        "slopes": [
          {
            "start": 250.0,
            "length": 100.0,
            "slope": 20000.0
          },
          {
            "start": 350.0,
            "length": 225.0,
            "slope": 10000.0
          },
          {
            "start": 575.0,
            "length": 150.0,
            "slope": -20000.0
          }
        ]
      },
      "10804": {
        "raceTrackId": 10008,
        "name": "芝1600m(内)",
        "distance": 1600,
        "distanceType": 2,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          1
        ],
        "laneMax": 14100,
        "finishTimeMin": 90.8,
        "finishTimeMax": 95.0,
        "corners": [
          {
            "start": 720.0,
            "length": 275.0
          },
          {
            "start": 995.0,
            "length": 277.0
          }
        ],
        "straights": [
          {
            "start": 200.0,
            "end": 720.0
          },
          {
            "start": 1272.0,
            "end": 1600.0
          }
        ],
        "slopes": [
          {
            "start": 520.0,
            "length": 175.0,
            "slope": 15000.0
          },
          {
            "start": 820.0,
            "length": 150.0,
            "slope": -15000.0
          }
        ]
      },
      "10805": {
        "raceTrackId": 10008,
        "name": "芝1600m(外)",
        "distance": 1600,
        "distanceType": 2,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          1
        ],
        "laneMax": 14100,
        "finishTimeMin": 90.8,
        "finishTimeMax": 95.0,
        "corners": [
          {
            "start": 700.0,
            "length": 250.0
          },
          {
            "start": 950.0,
            "length": 247.0
          }
        ],
        "straights": [
          {
            "start": 200.0,
            "end": 700.0
          },
          {
            "start": 1197.0,
            "end": 1600.0
          }
        ],
        "slopes": [
          {
            "start": 450.0,
            "length": 100.0,
            "slope": 20000.0
          },
          {
            "start": 550.0,
            "length": 225.0,
            "slope": 10000.0
          },
          {
            "start": 775.0,
            "length": 150.0,
            "slope": -20000.0
          }
        ]
      },
      "10806": {
        "raceTrackId": 10008,
        "name": "芝1800m(外)",
        "distance": 1800,
        "distanceType": 2,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 14100,
        "finishTimeMin": 104.4,
        "finishTimeMax": 110.0,
        "corners": [
          {
            "start": 900.0,
            "length": 250.0
          },
          {
            "start": 1150.0,
            "length": 247.0
          }
        ],
        "straights": [
          {
            "start": 400.0,
            "end": 900.0
          },
          {
            "start": 1397.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 650.0,
            "length": 100.0,
            "slope": 20000.0
          },
          {
            "start": 750.0,
            "length": 225.0,
            "slope": 10000.0
          },
          {
            "start": 975.0,
            "length": 150.0,
            "slope": -20000.0
          }
        ]
      },
      "10807": {
        "raceTrackId": 10008,
        "name": "芝2000m(内)",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 14100,
        "finishTimeMin": 117.1,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 400.0,
            "length": 185.0
          },
          {
            "start": 585.0,
            "length": 185.0
          },
          {
            "start": 1120.0,
            "length": 275.0
          },
          {
            "start": 1395.0,
            "length": 277.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 400.0
          },
          {
            "start": 770.0,
            "end": 1120.0
          },
          {
            "start": 1672.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 970.0,
            "length": 175.0,
            "slope": 15000.0
          },
          {
            "start": 1270.0,
            "length": 150.0,
            "slope": -15000.0
          }
        ]
      },
      "10808": {
        "raceTrackId": 10008,
        "name": "芝2200m(外)",
        "distance": 2200,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          1
        ],
        "laneMax": 14100,
        "finishTimeMin": 130.2,
        "finishTimeMax": 135.0,
        "corners": [
          {
            "start": 400.0,
            "length": 200.0
          },
          {
            "start": 600.0,
            "length": 200.0
          },
          {
            "start": 1300.0,
            "length": 250.0
          },
          {
            "start": 1550.0,
            "length": 247.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 400.0
          },
          {
            "start": 800.0,
            "end": 1300.0
          },
          {
            "start": 1797.0,
            "end": 2200.0
          }
        ],
        "slopes": [
          {
            "start": 1050.0,
            "length": 100.0,
            "slope": 20000.0
          },
          {
            "start": 1150.0,
            "length": 225.0,
            "slope": 10000.0
          },
          {
            "start": 1375.0,
            "length": 150.0,
            "slope": -20000.0
          }
        ]
      },
      "10809": {
        "raceTrackId": 10008,
        "name": "芝2400m(外)",
        "distance": 2400,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 14100,
        "finishTimeMin": 141.6,
        "finishTimeMax": 149.0,
        "corners": [
          {
            "start": 600.0,
            "length": 200.0
          },
          {
            "start": 800.0,
            "length": 200.0
          },
          {
            "start": 1500.0,
            "length": 250.0
          },
          {
            "start": 1750.0,
            "length": 247.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 600.0
          },
          {
            "start": 1000.0,
            "end": 1500.0
          },
          {
            "start": 1997.0,
            "end": 2400.0
          }
        ],
        "slopes": [
          {
            "start": 1350.0,
            "length": 225.0,
            "slope": 10000.0
          },
          {
            "start": 1250.0,
            "length": 100.0,
            "slope": 20000.0
          },
          {
            "start": 1575.0,
            "length": 150.0,
            "slope": -20000.0
          }
        ]
      },
      "10810": {
        "raceTrackId": 10008,
        "name": "芝3000m(外)",
        "distance": 3000,
        "distanceType": 4,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          3,
          5
        ],
        "laneMax": 14100,
        "finishTimeMin": 181.5,
        "finishTimeMax": 190.0,
        "corners": [
          {
            "start": 261.0,
            "length": 250.0
          },
          {
            "start": 511.0,
            "length": 250.0
          },
          {
            "start": 1250.0,
            "length": 200.0
          },
          {
            "start": 1450.0,
            "length": 200.0
          },
          {
            "start": 2100.0,
            "length": 250.0
          },
          {
            "start": 2350.0,
            "length": 247.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 261.0
          },
          {
            "start": 761.0,
            "end": 1250.0
          },
          {
            "start": 1650.0,
            "end": 2100.0
          },
          {
            "start": 2597.0,
            "end": 3000.0
          }
        ],
        "slopes": [
          {
            "start": 11.0,
            "length": 100.0,
            "slope": 20000.0
          },
          {
            "start": 111.0,
            "length": 225.0,
            "slope": 10000.0
          },
          {
            "start": 336.0,
            "length": 150.0,
            "slope": -20000.0
          },
          {
            "start": 1950.0,
            "length": 225.0,
            "slope": 10000.0
          },
          {
            "start": 1850.0,
            "length": 100.0,
            "slope": 20000.0
          },
          {
            "start": 2175.0,
            "length": 150.0,
            "slope": -20000.0
          }
        ]
      },
      "10811": {
        "raceTrackId": 10008,
        "name": "芝3200m(外)",
        "distance": 3200,
        "distanceType": 4,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 14100,
        "finishTimeMin": 193.0,
        "finishTimeMax": 204.0,
        "corners": [
          {
            "start": 458.0,
            "length": 250.0
          },
          {
            "start": 708.0,
            "length": 250.0
          },
          {
            "start": 1450.0,
            "length": 200.0
          },
          {
            "start": 1650.0,
            "length": 200.0
          },
          {
            "start": 2300.0,
            "length": 250.0
          },
          {
            "start": 2550.0,
            "length": 247.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 458.0
          },
          {
            "start": 958.0,
            "end": 1450.0
          },
          {
            "start": 1850.0,
            "end": 2300.0
          },
          {
            "start": 2797.0,
            "end": 3200.0
          }
        ],
        "slopes": [
          {
            "start": 208.0,
            "length": 100.0,
            "slope": 20000.0
          },
          {
            "start": 308.0,
            "length": 225.0,
            "slope": 10000.0
          },
          {
            "start": 533.0,
            "length": 150.0,
            "slope": -20000.0
          },
          {
            "start": 2050.0,
            "length": 100.0,
            "slope": 20000.0
          },
          {
            "start": 2375.0,
            "length": 150.0,
            "slope": -20000.0
          },
          {
            "start": 2150.0,
            "length": 225.0,
            "slope": 10000.0
          }
        ]
      },
      "10812": {
        "raceTrackId": 10008,
        "name": "ダート1200m",
        "distance": 1200,
        "distanceType": 1,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 13000,
        "finishTimeMin": 69.0,
        "finishTimeMax": 77.0,
        "corners": [
          {
            "start": 400.0,
            "length": 225.0
          },
          {
            "start": 625.0,
            "length": 246.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 400.0
          },
          {
            "start": 871.0,
            "end": 1200.0
          }
        ],
        "slopes": [
          {
            "start": 175.0,
            "length": 200.0,
            "slope": 15000.0
          },
          {
            "start": 475.0,
            "length": 200.0,
            "slope": -15000.0
          }
        ]
      },
      "10813": {
        "raceTrackId": 10008,
        "name": "ダート1400m",
        "distance": 1400,
        "distanceType": 1,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 13000,
        "finishTimeMin": 80.8,
        "finishTimeMax": 94.0,
        "corners": [
          {
            "start": 600.0,
            "length": 225.0
          },
          {
            "start": 825.0,
            "length": 246.0
          }
        ],
        "straights": [
          {
            "start": 100.0,
            "end": 600.0
          },
          {
            "start": 1071.0,
            "end": 1400.0
          }
        ],
        "slopes": [
          {
            "start": 375.0,
            "length": 200.0,
            "slope": 15000.0
          },
          {
            "start": 675.0,
            "length": 200.0,
            "slope": -15000.0
          }
        ]
      },
      "10814": {
        "raceTrackId": 10008,
        "name": "ダート1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 13000,
        "finishTimeMin": 108.1,
        "finishTimeMax": 118.0,
        "corners": [
          {
            "start": 300.0,
            "length": 150.0
          },
          {
            "start": 450.0,
            "length": 150.0
          },
          {
            "start": 1000.0,
            "length": 225.0
          },
          {
            "start": 1225.0,
            "length": 246.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 300.0
          },
          {
            "start": 600.0,
            "end": 1000.0
          },
          {
            "start": 1471.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 775.0,
            "length": 200.0,
            "slope": 15000.0
          },
          {
            "start": 1075.0,
            "length": 200.0,
            "slope": -15000.0
          }
        ]
      },
      "10815": {
        "raceTrackId": 10008,
        "name": "ダート1900m",
        "distance": 1900,
        "distanceType": 3,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 13000,
        "finishTimeMin": 114.2,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 400.0,
            "length": 150.0
          },
          {
            "start": 550.0,
            "length": 150.0
          },
          {
            "start": 1100.0,
            "length": 225.0
          },
          {
            "start": 1325.0,
            "length": 246.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 400.0
          },
          {
            "start": 700.0,
            "end": 1100.0
          },
          {
            "start": 1571.0,
            "end": 1900.0
          }
        ],
        "slopes": [
          {
            "start": 875.0,
            "length": 200.0,
            "slope": 15000.0
          },
          {
            "start": 1175.0,
            "length": 200.0,
            "slope": -15000.0
          }
        ]
      }
    }
  },
  "10009": {
    "name": "阪神",
    "courses": {
      "10901": {
        "raceTrackId": 10009,
        "name": "芝1200m(内)",
        "distance": 1200,
        "distanceType": 1,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 12500,
        "finishTimeMin": 67.5,
        "finishTimeMax": 71.0,
        "corners": [
          {
            "start": 250.0,
            "length": 300.0
          },
          {
            "start": 550.0,
            "length": 294.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 250.0
          },
          {
            "start": 844.0,
            "end": 1200.0
          }
        ],
        "slopes": [
          {
            "start": 400.0,
            "length": 595.0,
            "slope": -10000.0
          },
          {
            "start": 1000.0,
            "length": 125.0,
            "slope": 20000.0
          }
        ]
      },
      "10902": {
        "raceTrackId": 10009,
        "name": "芝1400m(内)",
        "distance": 1400,
        "distanceType": 1,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 12500,
        "finishTimeMin": 80.0,
        "finishTimeMax": 84.0,
        "corners": [
          {
            "start": 450.0,
            "length": 300.0
          },
          {
            "start": 750.0,
            "length": 294.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 450.0
          },
          {
            "start": 1044.0,
            "end": 1400.0
          }
        ],
        "slopes": [
          {
            "start": 600.0,
            "length": 595.0,
            "slope": -10000.0
          },
          {
            "start": 1200.0,
            "length": 125.0,
            "slope": 20000.0
          }
        ]
      },
      "10903": {
        "raceTrackId": 10009,
        "name": "芝1600m(外)",
        "distance": 1600,
        "distanceType": 2,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 12500,
        "finishTimeMin": 90.8,
        "finishTimeMax": 95.0,
        "corners": [
          {
            "start": 450.0,
            "length": 350.0
          },
          {
            "start": 800.0,
            "length": 327.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 450.0
          },
          {
            "start": 1127.0,
            "end": 1600.0
          }
        ],
        "slopes": [
          {
            "start": 950.0,
            "length": 400.0,
            "slope": -10000.0
          },
          {
            "start": 1405.0,
            "length": 120.0,
            "slope": 20000.0
          }
        ]
      },
      "10904": {
        "raceTrackId": 10009,
        "name": "芝1800m(外)",
        "distance": 1800,
        "distanceType": 2,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 12500,
        "finishTimeMin": 104.4,
        "finishTimeMax": 110.0,
        "corners": [
          {
            "start": 650.0,
            "length": 350.0
          },
          {
            "start": 1000.0,
            "length": 327.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 650.0
          },
          {
            "start": 1327.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 1150.0,
            "length": 400.0,
            "slope": -10000.0
          },
          {
            "start": 1605.0,
            "length": 120.0,
            "slope": 20000.0
          }
        ]
      },
      "10905": {
        "raceTrackId": 10009,
        "name": "芝2000m(内)",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          4
        ],
        "laneMax": 12500,
        "finishTimeMin": 117.1,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 370.0,
            "length": 190.0
          },
          {
            "start": 560.0,
            "length": 190.0
          },
          {
            "start": 1050.0,
            "length": 300.0
          },
          {
            "start": 1350.0,
            "length": 294.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 370.0
          },
          {
            "start": 750.0,
            "end": 1050.0
          },
          {
            "start": 1644.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 140.0,
            "slope": -10000.0
          },
          {
            "start": 145.0,
            "length": 125.0,
            "slope": 20000.0
          },
          {
            "start": 1200.0,
            "length": 595.0,
            "slope": -10000.0
          },
          {
            "start": 1800.0,
            "length": 125.0,
            "slope": 20000.0
          }
        ]
      },
      "10906": {
        "raceTrackId": 10009,
        "name": "芝2200m(内)",
        "distance": 2200,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          1
        ],
        "laneMax": 12500,
        "finishTimeMin": 130.2,
        "finishTimeMax": 135.0,
        "corners": [
          {
            "start": 520.0,
            "length": 190.0
          },
          {
            "start": 710.0,
            "length": 190.0
          },
          {
            "start": 1250.0,
            "length": 300.0
          },
          {
            "start": 1550.0,
            "length": 294.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 520.0
          },
          {
            "start": 900.0,
            "end": 1250.0
          },
          {
            "start": 1844.0,
            "end": 2200.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 290.0,
            "slope": -10000.0
          },
          {
            "start": 295.0,
            "length": 125.0,
            "slope": 20000.0
          },
          {
            "start": 1400.0,
            "length": 595.0,
            "slope": -10000.0
          },
          {
            "start": 2000.0,
            "length": 125.0,
            "slope": 20000.0
          }
        ]
      },
      "10907": {
        "raceTrackId": 10009,
        "name": "芝2400m(外)",
        "distance": 2400,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 12500,
        "finishTimeMin": 141.6,
        "finishTimeMax": 149.0,
        "corners": [
          {
            "start": 382.0,
            "length": 190.0
          },
          {
            "start": 558.0,
            "length": 190.0
          },
          {
            "start": 1250.0,
            "length": 350.0
          },
          {
            "start": 1600.0,
            "length": 327.0
          }
        ],
        "straights": [
          {
            "start": 750.0,
            "end": 1250.0
          },
          {
            "start": 1927.0,
            "end": 2400.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 132.0,
            "slope": -10000.0
          },
          {
            "start": 187.0,
            "length": 120.0,
            "slope": 20000.0
          },
          {
            "start": 1750.0,
            "length": 400.0,
            "slope": -10000.0
          },
          {
            "start": 2205.0,
            "length": 120.0,
            "slope": 20000.0
          }
        ]
      },
      "10908": {
        "raceTrackId": 10009,
        "name": "芝2600m(外)",
        "distance": 2600,
        "distanceType": 4,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 12500,
        "finishTimeMin": 157.6,
        "finishTimeMax": 165.0,
        "corners": [
          {
            "start": 570.0,
            "length": 190.0
          },
          {
            "start": 760.0,
            "length": 190.0
          },
          {
            "start": 1450.0,
            "length": 350.0
          },
          {
            "start": 1800.0,
            "length": 327.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 570.0
          },
          {
            "start": 950.0,
            "end": 1450.0
          },
          {
            "start": 2127.0,
            "end": 2600.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 315.0,
            "slope": -10000.0
          },
          {
            "start": 370.0,
            "length": 120.0,
            "slope": 20000.0
          },
          {
            "start": 1950.0,
            "length": 400.0,
            "slope": -10000.0
          },
          {
            "start": 2405.0,
            "length": 120.0,
            "slope": 20000.0
          }
        ]
      },
      "10909": {
        "raceTrackId": 10009,
        "name": "芝3000m(内)",
        "distance": 3000,
        "distanceType": 4,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 12500,
        "finishTimeMin": 181.5,
        "finishTimeMax": 190.0,
        "corners": [
          {
            "start": 348.0,
            "length": 300.0
          },
          {
            "start": 648.0,
            "length": 300.0
          },
          {
            "start": 1320.0,
            "length": 190.0
          },
          {
            "start": 1510.0,
            "length": 190.0
          },
          {
            "start": 2050.0,
            "length": 300.0
          },
          {
            "start": 2350.0,
            "length": 294.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 348.0
          },
          {
            "start": 948.0,
            "end": 1320.0
          },
          {
            "start": 1700.0,
            "end": 2050.0
          },
          {
            "start": 2644.0,
            "end": 3000.0
          }
        ],
        "slopes": [
          {
            "start": 498.0,
            "length": 595.0,
            "slope": -10000.0
          },
          {
            "start": 1095.0,
            "length": 125.0,
            "slope": 20000.0
          },
          {
            "start": 2200.0,
            "length": 595.0,
            "slope": -10000.0
          },
          {
            "start": 2800.0,
            "length": 125.0,
            "slope": 20000.0
          }
        ]
      },
      "10910": {
        "raceTrackId": 10009,
        "name": "ダート1200m",
        "distance": 1200,
        "distanceType": 1,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 12000,
        "finishTimeMin": 69.0,
        "finishTimeMax": 77.0,
        "corners": [
          {
            "start": 350.0,
            "length": 250.0
          },
          {
            "start": 600.0,
            "length": 248.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 350.0
          },
          {
            "start": 848.0,
            "end": 1200.0
          }
        ],
        "slopes": []
      },
      "10911": {
        "raceTrackId": 10009,
        "name": "ダート1400m",
        "distance": 1400,
        "distanceType": 1,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 12000,
        "finishTimeMin": 80.8,
        "finishTimeMax": 94.0,
        "corners": [
          {
            "start": 550.0,
            "length": 250.0
          },
          {
            "start": 800.0,
            "length": 248.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 550.0
          },
          {
            "start": 1048.0,
            "end": 1400.0
          }
        ],
        "slopes": [
          {
            "start": 1200.0,
            "length": 125.0,
            "slope": 15000.0
          }
        ]
      },
      "10912": {
        "raceTrackId": 10009,
        "name": "ダート1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 12000,
        "finishTimeMin": 108.1,
        "finishTimeMax": 118.0,
        "corners": [
          {
            "start": 330.0,
            "length": 150.0
          },
          {
            "start": 480.0,
            "length": 150.0
          },
          {
            "start": 950.0,
            "length": 250.0
          },
          {
            "start": 1200.0,
            "length": 248.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 330.0
          },
          {
            "start": 630.0,
            "end": 950.0
          },
          {
            "start": 1448.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 105.0,
            "length": 125.0,
            "slope": 15000.0
          },
          {
            "start": 1599.0,
            "length": 125.0,
            "slope": 15000.0
          }
        ]
      },
      "10913": {
        "raceTrackId": 10009,
        "name": "ダート2000m",
        "distance": 2000,
        "distanceType": 3,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [
          2,
          3
        ],
        "laneMax": 12000,
        "finishTimeMin": 121.9,
        "finishTimeMax": 129.0,
        "corners": [
          {
            "start": 500.0,
            "length": 150.0
          },
          {
            "start": 650.0,
            "length": 150.0
          },
          {
            "start": 1150.0,
            "length": 250.0
          },
          {
            "start": 1400.0,
            "length": 248.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 500.0
          },
          {
            "start": 800.0,
            "end": 1150.0
          },
          {
            "start": 1648.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 275.0,
            "length": 125.0,
            "slope": 15000.0
          },
          {
            "start": 1800.0,
            "length": 125.0,
            "slope": 15000.0
          }
        ]
      },
      "10914": {
        "raceTrackId": 10009,
        "name": "芝3200m(外→内)",
        "distance": 3200,
        "distanceType": 4,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 12500,
        "finishTimeMin": 193.0,
        "finishTimeMax": 204.0,
        "corners": [
          {
            "start": 370.0,
            "length": 350.0
          },
          {
            "start": 720.0,
            "length": 350.0
          },
          {
            "start": 1520.0,
            "length": 190.0
          },
          {
            "start": 1710.0,
            "length": 190.0
          },
          {
            "start": 2250.0,
            "length": 300.0
          },
          {
            "start": 2550.0,
            "length": 294.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 370.0
          },
          {
            "start": 1070.0,
            "end": 1520.0
          },
          {
            "start": 1900.0,
            "end": 2250.0
          },
          {
            "start": 2844.0,
            "end": 3200.0
          }
        ],
        "slopes": [
          {
            "start": 870.0,
            "length": 400.0,
            "slope": -10000.0
          },
          {
            "start": 1325.0,
            "length": 120.0,
            "slope": 20000.0
          },
          {
            "start": 2400.0,
            "length": 595.0,
            "slope": -10000.0
          },
          {
            "start": 3000.0,
            "length": 125.0,
            "slope": 20000.0
          }
        ]
      }
    }
  },
  "10010": {
    "name": "小倉",
    "courses": {
      "11001": {
        "raceTrackId": 10010,
        "name": "芝1200m",
        "distance": 1200,
        "distanceType": 1,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          1
        ],
        "laneMax": 14500,
        "finishTimeMin": 67.5,
        "finishTimeMax": 71.0,
        "corners": [
          {
            "start": 500.0,
            "length": 205.0
          },
          {
            "start": 705.0,
            "length": 202.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 500.0
          },
          {
            "start": 907.0,
            "end": 1200.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 60.0,
            "slope": -15000.0
          }
        ]
      },
      "11002": {
        "raceTrackId": 10010,
        "name": "芝1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 14500,
        "finishTimeMin": 104.4,
        "finishTimeMax": 110.0,
        "corners": [
          {
            "start": 290.0,
            "length": 205.0
          },
          {
            "start": 495.0,
            "length": 205.0
          },
          {
            "start": 1100.0,
            "length": 205.0
          },
          {
            "start": 1305.0,
            "length": 202.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 290.0
          },
          {
            "start": 700.0,
            "end": 1100.0
          },
          {
            "start": 1507.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 280.0,
            "length": 255.0,
            "slope": 15000.0
          }
        ]
      },
      "11003": {
        "raceTrackId": 10010,
        "name": "芝2000m",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 14500,
        "finishTimeMin": 117.1,
        "finishTimeMax": 123.0,
        "corners": [
          {
            "start": 490.0,
            "length": 205.0
          },
          {
            "start": 695.0,
            "length": 205.0
          },
          {
            "start": 1300.0,
            "length": 205.0
          },
          {
            "start": 1505.0,
            "length": 202.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 490.0
          },
          {
            "start": 900.0,
            "end": 1300.0
          },
          {
            "start": 1707.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 480.0,
            "length": 255.0,
            "slope": 15000.0
          }
        ]
      },
      "11004": {
        "raceTrackId": 10010,
        "name": "芝2600m",
        "distance": 2600,
        "distanceType": 4,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          2
        ],
        "laneMax": 14500,
        "finishTimeMin": 157.6,
        "finishTimeMax": 165.0,
        "corners": [
          {
            "start": 309.0,
            "length": 205.0
          },
          {
            "start": 514.0,
            "length": 205.0
          },
          {
            "start": 1110.0,
            "length": 205.0
          },
          {
            "start": 1315.0,
            "length": 205.0
          },
          {
            "start": 1900.0,
            "length": 205.0
          },
          {
            "start": 2105.0,
            "length": 202.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 309.0
          },
          {
            "start": 719.0,
            "end": 1110.0
          },
          {
            "start": 1520.0,
            "end": 1900.0
          },
          {
            "start": 2307.0,
            "end": 2600.0
          }
        ],
        "slopes": [
          {
            "start": 1100.0,
            "length": 255.0,
            "slope": 15000.0
          }
        ]
      },
      "11005": {
        "raceTrackId": 10010,
        "name": "ダート1000m",
        "distance": 1000,
        "distanceType": 1,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [
          1
        ],
        "laneMax": 12000,
        "finishTimeMin": 57.4,
        "finishTimeMax": 63.0,
        "corners": [
          {
            "start": 360.0,
            "length": 180.0
          },
          {
            "start": 540.0,
            "length": 169.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 360.0
          },
          {
            "start": 709.0,
            "end": 1000.0
          }
        ],
        "slopes": []
      },
      "11006": {
        "raceTrackId": 10010,
        "name": "ダート1700m",
        "distance": 1700,
        "distanceType": 2,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 12000,
        "finishTimeMin": 101.4,
        "finishTimeMax": 113.0,
        "corners": [
          {
            "start": 340.0,
            "length": 180.0
          },
          {
            "start": 520.0,
            "length": 180.0
          },
          {
            "start": 1060.0,
            "length": 180.0
          },
          {
            "start": 1240.0,
            "length": 169.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 340.0
          },
          {
            "start": 700.0,
            "end": 1060.0
          },
          {
            "start": 1409.0,
            "end": 1700.0
          }
        ],
        "slopes": [
          {
            "start": 370.0,
            "length": 150.0,
            "slope": 15000.0
          }
        ]
      },
      "11007": {
        "raceTrackId": 10010,
        "name": "ダート2400m",
        "distance": 2400,
        "distanceType": 3,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [],
        "laneMax": 12000,
        "finishTimeMin": 149.1,
        "finishTimeMax": 156.0,
        "corners": [
          {
            "start": 312.0,
            "length": 180.0
          },
          {
            "start": 492.0,
            "length": 180.0
          },
          {
            "start": 1040.0,
            "length": 180.0
          },
          {
            "start": 1220.0,
            "length": 180.0
          },
          {
            "start": 1760.0,
            "length": 180.0
          },
          {
            "start": 1940.0,
            "length": 169.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 312.0
          },
          {
            "start": 672.0,
            "end": 1040.0
          },
          {
            "start": 1400.0,
            "end": 1760.0
          },
          {
            "start": 2109.0,
            "end": 2400.0
          }
        ],
        "slopes": []
      }
    }
  },
  "10101": {
    "name": "大井",
    "courses": {
      "11101": {
        "raceTrackId": 10101,
        "name": "ダート1200m",
        "distance": 1200,
        "distanceType": 1,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [
          4,
          5
        ],
        "laneMax": 12000,
        "finishTimeMin": 69.0,
        "finishTimeMax": 77.0,
        "corners": [
          {
            "start": 500.0,
            "length": 150.0
          },
          {
            "start": 650.0,
            "length": 164.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 500.0
          },
          {
            "start": 814.0,
            "end": 1200.0
          }
        ],
        "slopes": []
      },
      "11102": {
        "raceTrackId": 10101,
        "name": "ダート1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [
          3
        ],
        "laneMax": 12000,
        "finishTimeMin": 108.1,
        "finishTimeMax": 118.0,
        "corners": [
          {
            "start": 300.0,
            "length": 150.0
          },
          {
            "start": 500.0,
            "length": 150.0
          },
          {
            "start": 1100.0,
            "length": 150.0
          },
          {
            "start": 1250.0,
            "length": 164.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 301.0
          },
          {
            "start": 600.0,
            "end": 1100.22998046875
          },
          {
            "start": 1414.0,
            "end": 1800.0
          }
        ],
        "slopes": []
      },
      "11103": {
        "raceTrackId": 10101,
        "name": "ダート2000m",
        "distance": 2000,
        "distanceType": 3,
        "surface": 2,
        "turn": 1,
        "courseSetStatus": [
          2
        ],
        "laneMax": 12000,
        "finishTimeMin": 121.9,
        "finishTimeMax": 129.0,
        "corners": [
          {
            "start": 500.0,
            "length": 150.0
          },
          {
            "start": 650.0,
            "length": 150.0
          },
          {
            "start": 1300.0,
            "length": 150.0
          },
          {
            "start": 1450.0,
            "length": 164.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 500.0
          },
          {
            "start": 800.0,
            "end": 1300.0
          },
          {
            "start": 1614.0,
            "end": 2000.0
          }
        ],
        "slopes": []
      }
    }
  },
  "10201": {
    "name": "ロンシャン",
    "courses": {
      "11201": {
        "raceTrackId": 10201,
        "name": "芝1000m",
        "distance": 1000,
        "distanceType": 1,
        "surface": 1,
        "turn": 4,
        "courseSetStatus": [],
        "laneMax": 12000,
        "finishTimeMin": 54.7,
        "finishTimeMax": 57.0,
        "corners": [
          {
            "start": 200.0,
            "length": 200.0
          },
          {
            "start": 400.0,
            "length": 200.0
          }
        ],
        "straights": [],
        "slopes": []
      },
      "11203": {
        "raceTrackId": 10201,
        "name": "芝2400m",
        "distance": 2400,
        "distanceType": 3,
        "surface": 1,
        "turn": 1,
        "courseSetStatus": [
          2,
          3
        ],
        "laneMax": 12000,
        "finishTimeMin": 145.0,
        "finishTimeMax": 165.0,
        "corners": [
          {
            "start": 1000.0,
            "length": 417.0
          },
          {
            "start": 1417.0,
            "length": 200.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 1000.0
          },
          {
            "start": 1617.0,
            "end": 1866.0
          },
          {
            "start": 1867.0,
            "end": 2400.0
          }
        ],
        "slopes": [
          {
            "start": 400.0,
            "length": 600.0,
            "slope": 20000.0
          },
          {
            "start": 1017.0,
            "length": 383.0,
            "slope": -20000.0
          },
          {
            "start": 1400.0,
            "length": 217.0,
            "slope": -15000.0
          }
        ]
      }
    }
  },
  "10103": {
    "name": "川崎",
    "courses": {
      "11301": {
        "raceTrackId": 10103,
        "name": "ダート1400m",
        "distance": 1400,
        "distanceType": 1,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          5
        ],
        "laneMax": 13500,
        "finishTimeMin": 87.0,
        "finishTimeMax": 94.0,
        "corners": [
          {
            "start": 300.0,
            "length": 100.0
          },
          {
            "start": 400.0,
            "length": 100.0
          },
          {
            "start": 900.0,
            "length": 100.0
          },
          {
            "start": 1000.0,
            "length": 100.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 300.0
          },
          {
            "start": 500.0,
            "end": 900.0
          },
          {
            "start": 1100.0,
            "end": 1400.0
          }
        ],
        "slopes": []
      },
      "11302": {
        "raceTrackId": 10103,
        "name": "ダート1600m",
        "distance": 1600,
        "distanceType": 2,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          5
        ],
        "laneMax": 13500,
        "finishTimeMin": 98.0,
        "finishTimeMax": 108.0,
        "corners": [
          {
            "start": 500.0,
            "length": 100.0
          },
          {
            "start": 600.0,
            "length": 100.0
          },
          {
            "start": 1100.0,
            "length": 100.0
          },
          {
            "start": 1200.0,
            "length": 100.0
          }
        ],
        "straights": [
          {
            "start": 100.0,
            "end": 500.0
          },
          {
            "start": 700.0,
            "end": 1100.0
          },
          {
            "start": 1300.0,
            "end": 1600.0
          }
        ],
        "slopes": []
      },
      "11303": {
        "raceTrackId": 10103,
        "name": "ダート2100m",
        "distance": 2100,
        "distanceType": 3,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          2,
          5
        ],
        "laneMax": 13500,
        "finishTimeMin": 131.0,
        "finishTimeMax": 140.0,
        "corners": [
          {
            "start": 400.0,
            "length": 100.0
          },
          {
            "start": 500.0,
            "length": 100.0
          },
          {
            "start": 1000.0,
            "length": 100.0
          },
          {
            "start": 1100.0,
            "length": 100.0
          },
          {
            "start": 1600.0,
            "length": 100.0
          },
          {
            "start": 1700.0,
            "length": 100.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 400.0
          },
          {
            "start": 600.0,
            "end": 1000.0
          },
          {
            "start": 1200.0,
            "end": 1600.0
          },
          {
            "start": 1800.0,
            "end": 2100.0
          }
        ],
        "slopes": []
      }
    }
  },
  "10104": {
    "name": "船橋",
    "courses": {
      "11401": {
        "raceTrackId": 10104,
        "name": "ダート1000m",
        "distance": 1000,
        "distanceType": 1,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          1
        ],
        "laneMax": 13500,
        "finishTimeMin": 58.0,
        "finishTimeMax": 63.0,
        "corners": [
          {
            "start": 370.0,
            "length": 170.0
          },
          {
            "start": 540.0,
            "length": 152.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 370.0
          },
          {
            "start": 692.0,
            "end": 1000.0
          }
        ],
        "slopes": []
      },
      "11402": {
        "raceTrackId": 10104,
        "name": "ダート1600m",
        "distance": 1600,
        "distanceType": 2,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 13500,
        "finishTimeMin": 96.0,
        "finishTimeMax": 108.0,
        "corners": [
          {
            "start": 260.0,
            "length": 180.0
          },
          {
            "start": 440.0,
            "length": 160.0
          },
          {
            "start": 970.0,
            "length": 170.0
          },
          {
            "start": 1140.0,
            "length": 152.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 260.0
          },
          {
            "start": 600.0,
            "end": 970.0
          },
          {
            "start": 1292.0,
            "end": 1600.0
          }
        ],
        "slopes": []
      },
      "11403": {
        "raceTrackId": 10104,
        "name": "ダート1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 13500,
        "finishTimeMin": 108.0,
        "finishTimeMax": 118.0,
        "corners": [
          {
            "start": 460.0,
            "length": 180.0
          },
          {
            "start": 640.0,
            "length": 160.0
          },
          {
            "start": 1170.0,
            "length": 170.0
          },
          {
            "start": 1340.0,
            "length": 152.0
          }
        ],
        "straights": [
          {
            "start": 90.0,
            "end": 460.0
          },
          {
            "start": 800.0,
            "end": 1170.0
          },
          {
            "start": 1492.0,
            "end": 1800.0
          }
        ],
        "slopes": []
      },
      "11404": {
        "raceTrackId": 10104,
        "name": "ダート2400m",
        "distance": 2400,
        "distanceType": 3,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          2
        ],
        "laneMax": 13500,
        "finishTimeMin": 150.0,
        "finishTimeMax": 156.0,
        "corners": [
          {
            "start": 370.0,
            "length": 170.0
          },
          {
            "start": 540.0,
            "length": 150.0
          },
          {
            "start": 1060.0,
            "length": 180.0
          },
          {
            "start": 1240.0,
            "length": 160.0
          },
          {
            "start": 1770.0,
            "length": 170.0
          },
          {
            "start": 1940.0,
            "length": 152.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 370.0
          },
          {
            "start": 690.0,
            "end": 1060.0
          },
          {
            "start": 1400.0,
            "end": 1770.0
          },
          {
            "start": 2092.0,
            "end": 2400.0
          }
        ],
        "slopes": []
      }
    }
  },
  "10105": {
    "name": "盛岡",
    "courses": {
      "11501": {
        "raceTrackId": 10105,
        "name": "ダート1200m",
        "distance": 1200,
        "distanceType": 1,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          2
        ],
        "laneMax": 13500,
        "finishTimeMin": 69.0,
        "finishTimeMax": 77.0,
        "corners": [
          {
            "start": 500.0,
            "length": 200.0
          },
          {
            "start": 700.0,
            "length": 200.0
          }
        ],
        "straights": [
          {
            "start": 100.0,
            "end": 500.0
          },
          {
            "start": 900.0,
            "end": 1200.0
          }
        ],
        "slopes": [
          {
            "start": 100.0,
            "length": 375.0,
            "slope": 10000.0
          },
          {
            "start": 475.0,
            "length": 450.0,
            "slope": -15000.0
          },
          {
            "start": 975.0,
            "length": 175.0,
            "slope": 10000.0
          }
        ]
      },
      "11502": {
        "raceTrackId": 10105,
        "name": "ダート1600m",
        "distance": 1600,
        "distanceType": 2,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          2,
          5
        ],
        "laneMax": 13500,
        "finishTimeMin": 93.0,
        "finishTimeMax": 108.0,
        "corners": [
          {
            "start": 900.0,
            "length": 200.0
          },
          {
            "start": 1100.0,
            "length": 200.0
          }
        ],
        "straights": [
          {
            "start": 500.0,
            "end": 900.0
          },
          {
            "start": 1300.0,
            "end": 1600.0
          }
        ],
        "slopes": [
          {
            "start": 500.0,
            "length": 375.0,
            "slope": 10000.0
          },
          {
            "start": 875.0,
            "length": 450.0,
            "slope": -15000.0
          },
          {
            "start": 1375.0,
            "length": 175.0,
            "slope": 10000.0
          }
        ]
      },
      "11503": {
        "raceTrackId": 10105,
        "name": "ダート1800m",
        "distance": 1800,
        "distanceType": 2,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          2,
          5
        ],
        "laneMax": 13500,
        "finishTimeMin": 110.0,
        "finishTimeMax": 118.0,
        "corners": [
          {
            "start": 300.0,
            "length": 200.0
          },
          {
            "start": 500.0,
            "length": 200.0
          },
          {
            "start": 1100.0,
            "length": 200.0
          },
          {
            "start": 1300.0,
            "length": 200.0
          }
        ],
        "straights": [
          {
            "start": 0.0,
            "end": 300.0
          },
          {
            "start": 700.0,
            "end": 1100.0
          },
          {
            "start": 1500.0,
            "end": 1800.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 150.0,
            "slope": 10000.0
          },
          {
            "start": 700.0,
            "length": 450.0,
            "slope": -15000.0
          },
          {
            "start": 1575.0,
            "length": 175.0,
            "slope": 10000.0
          }
        ]
      },
      "11504": {
        "raceTrackId": 10105,
        "name": "ダート2000m",
        "distance": 2000,
        "distanceType": 3,
        "surface": 2,
        "turn": 2,
        "courseSetStatus": [
          2
        ],
        "laneMax": 13500,
        "finishTimeMin": 121.0,
        "finishTimeMax": 129.0,
        "corners": [
          {
            "start": 500.0,
            "length": 200.0
          },
          {
            "start": 700.0,
            "length": 200.0
          },
          {
            "start": 1300.0,
            "length": 200.0
          },
          {
            "start": 1500.0,
            "length": 200.0
          }
        ],
        "straights": [
          {
            "start": 100.0,
            "end": 500.0
          },
          {
            "start": 900.0,
            "end": 1300.0
          },
          {
            "start": 1700.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 175.0,
            "length": 175.0,
            "slope": 10000.0
          },
          {
            "start": 900.0,
            "length": 375.0,
            "slope": 10000.0
          },
          {
            "start": 1275.0,
            "length": 450.0,
            "slope": -15000.0
          },
          {
            "start": 1775.0,
            "length": 175.0,
            "slope": 10000.0
          }
        ]
      }
    }
  },
  "10202": {
    "name": "サンタアニタパーク",
    "courses": {
      "11605": {
        "raceTrackId": 10202,
        "name": "芝2000m",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [],
        "laneMax": 12000,
        "finishTimeMin": 117.2,
        "finishTimeMax": 123.2,
        "corners": [
          {
            "start": 700.0,
            "length": 150.0
          },
          {
            "start": 850.0,
            "length": 150.0
          },
          {
            "start": 1400.0,
            "length": 150.0
          },
          {
            "start": 1550.0,
            "length": 150.0
          }
        ],
        "straights": [
          {
            "start": 300.0,
            "end": 700.0
          },
          {
            "start": 1000.0,
            "end": 1400.0
          },
          {
            "start": 1700.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 0.0,
            "length": 150.0,
            "slope": -20000.0
          }
        ]
      },
      "11612": {
        "raceTrackId": 10202,
        "name": "芝2000m",
        "distance": 2000,
        "distanceType": 3,
        "surface": 1,
        "turn": 2,
        "courseSetStatus": [
          2
        ],
        "laneMax": 135000,
        "finishTimeMin": 121.0,
        "finishTimeMax": 129.0,
        "corners": [
          {
            "start": 500.0,
            "length": 200.0
          },
          {
            "start": 700.0,
            "length": 200.0
          },
          {
            "start": 1300.0,
            "length": 200.0
          },
          {
            "start": 1500.0,
            "length": 200.0
          }
        ],
        "straights": [
          {
            "start": 100.0,
            "end": 500.0
          },
          {
            "start": 900.0,
            "end": 1300.0
          },
          {
            "start": 1700.0,
            "end": 2000.0
          }
        ],
        "slopes": [
          {
            "start": 175.0,
            "length": 175.0,
            "slope": 10000.0
          },
          {
            "start": 900.0,
            "length": 375.0,
            "slope": 10000.0
          },
          {
            "start": 1275.0,
            "length": 450.0,
            "slope": -15000.0
          },
          {
            "start": 1775.0,
            "length": 175.0,
            "slope": 10000.0
          }
        ]
      }
    }
  }
}
""".trimIndent()
