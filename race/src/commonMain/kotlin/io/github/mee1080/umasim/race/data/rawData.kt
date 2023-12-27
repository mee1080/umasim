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
        "finishTimeMax": 71,
        "corners": [
          {
            "start": 400,
            "length": 275
          },
          {
            "start": 675,
            "length": 259
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 400
          },
          {
            "start": 934,
            "end": 1200
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
        "finishTimeMax": 95,
        "corners": [
          {
            "start": 150,
            "length": 275
          },
          {
            "start": 700,
            "length": 275
          },
          {
            "start": 975,
            "length": 259
          }
        ],
        "straights": [
          {
            "start": 425,
            "end": 700
          },
          {
            "start": 1234,
            "end": 1500
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
        "finishTimeMax": 110,
        "corners": [
          {
            "start": 175,
            "length": 275
          },
          {
            "start": 450,
            "length": 275
          },
          {
            "start": 1000,
            "length": 275
          },
          {
            "start": 1275,
            "length": 259
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 175
          },
          {
            "start": 725,
            "end": 1000
          },
          {
            "start": 1534,
            "end": 1800
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 375,
            "length": 275
          },
          {
            "start": 650,
            "length": 275
          },
          {
            "start": 1200,
            "length": 275
          },
          {
            "start": 1475,
            "length": 259
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 375
          },
          {
            "start": 925,
            "end": 1200
          },
          {
            "start": 1734,
            "end": 2000
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
        "finishTimeMax": 165,
        "corners": [
          {
            "start": 175,
            "length": 275
          },
          {
            "start": 450,
            "length": 275
          },
          {
            "start": 975,
            "length": 275
          },
          {
            "start": 1250,
            "length": 275
          },
          {
            "start": 1800,
            "length": 275
          },
          {
            "start": 2075,
            "length": 259
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 175
          },
          {
            "start": 725,
            "end": 975
          },
          {
            "start": 1525,
            "end": 1800
          },
          {
            "start": 2334,
            "end": 2600
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
        "finishTimeMax": 63,
        "corners": [
          {
            "start": 280,
            "length": 230
          },
          {
            "start": 510,
            "length": 226
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 280
          },
          {
            "start": 736,
            "end": 1000
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
        "finishTimeMax": 113,
        "corners": [
          {
            "start": 240,
            "length": 230
          },
          {
            "start": 470,
            "length": 230
          },
          {
            "start": 980,
            "length": 230
          },
          {
            "start": 1210,
            "length": 226
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 240
          },
          {
            "start": 700,
            "end": 980
          },
          {
            "start": 1436,
            "end": 1700
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
        "finishTimeMax": 156,
        "corners": [
          {
            "start": 200,
            "length": 230
          },
          {
            "start": 430,
            "length": 230
          },
          {
            "start": 940,
            "length": 230
          },
          {
            "start": 1170,
            "length": 230
          },
          {
            "start": 1680,
            "length": 230
          },
          {
            "start": 1910,
            "length": 226
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 200
          },
          {
            "start": 660,
            "end": 940
          },
          {
            "start": 1408,
            "end": 1680
          },
          {
            "start": 2136,
            "end": 2400
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
        "finishTimeMax": 57,
        "corners": [
          {
            "start": 310,
            "length": 220
          },
          {
            "start": 530,
            "length": 208
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 310
          },
          {
            "start": 738,
            "end": 1000
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 555,
            "slope": 10000
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
        "finishTimeMax": 71,
        "corners": [
          {
            "start": 510,
            "length": 220
          },
          {
            "start": 730,
            "length": 208
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 510
          },
          {
            "start": 938,
            "end": 1200
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 755,
            "slope": 10000
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
        "finishTimeMax": 110,
        "corners": [
          {
            "start": 320,
            "length": 220
          },
          {
            "start": 540,
            "length": 220
          },
          {
            "start": 1110,
            "length": 220
          },
          {
            "start": 1330,
            "length": 208
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 320
          },
          {
            "start": 760,
            "end": 1110
          },
          {
            "start": 1538,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 220,
            "length": 200,
            "slope": -10000
          },
          {
            "start": 665,
            "length": 720,
            "slope": 10000
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 520,
            "length": 220
          },
          {
            "start": 740,
            "length": 220
          },
          {
            "start": 1310,
            "length": 220
          },
          {
            "start": 1530,
            "length": 208
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 520
          },
          {
            "start": 960,
            "end": 1310
          },
          {
            "start": 1738,
            "end": 2000
          }
        ],
        "slopes": [
          {
            "start": 420,
            "length": 200,
            "slope": -10000
          },
          {
            "start": 865,
            "length": 720,
            "slope": 10000
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
        "finishTimeMax": 165,
        "corners": [
          {
            "start": 260,
            "length": 240
          },
          {
            "start": 500,
            "length": 230
          },
          {
            "start": 1120,
            "length": 220
          },
          {
            "start": 1340,
            "length": 220
          },
          {
            "start": 1910,
            "length": 220
          },
          {
            "start": 2130,
            "length": 208
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 260
          },
          {
            "start": 730,
            "end": 1120
          },
          {
            "start": 1560,
            "end": 1910
          },
          {
            "start": 2338,
            "end": 2600
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 495,
            "slope": 10000
          },
          {
            "start": 970,
            "length": 200,
            "slope": -10000
          },
          {
            "start": 1425,
            "length": 720,
            "slope": 10000
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
        "finishTimeMax": 63,
        "corners": [
          {
            "start": 370,
            "length": 190
          },
          {
            "start": 560,
            "length": 180
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 370
          },
          {
            "start": 740,
            "end": 1000
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
        "finishTimeMax": 113,
        "corners": [
          {
            "start": 350,
            "length": 190
          },
          {
            "start": 540,
            "length": 190
          },
          {
            "start": 1070,
            "length": 190
          },
          {
            "start": 1260,
            "length": 180
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 350
          },
          {
            "start": 730,
            "end": 1070
          },
          {
            "start": 1440,
            "end": 1700
          }
        ],
        "slopes": [
          {
            "start": 275,
            "length": 340,
            "slope": -10000
          },
          {
            "start": 615,
            "length": 670,
            "slope": 10000
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
        "finishTimeMax": 156,
        "corners": [
          {
            "start": 292,
            "length": 190
          },
          {
            "start": 482,
            "length": 190
          },
          {
            "start": 1040,
            "length": 190
          },
          {
            "start": 1230,
            "length": 190
          },
          {
            "start": 1770,
            "length": 190
          },
          {
            "start": 1960,
            "length": 180
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 292
          },
          {
            "start": 672,
            "end": 1040
          },
          {
            "start": 1420,
            "end": 1770
          },
          {
            "start": 2140,
            "end": 2400
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
        "laneMax": 13500,
        "finishTimeMin": 54.7,
        "finishTimeMax": 57,
        "corners": [],
        "straights": [
          {
            "start": 0,
            "end": 649.9
          },
          {
            "start": 650,
            "end": 1000
          }
        ],
        "slopes": []
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
        "finishTimeMax": 71,
        "corners": [
          {
            "start": 450,
            "length": 200
          },
          {
            "start": 650,
            "length": 192
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 450
          },
          {
            "start": 842,
            "end": 1200
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
        "finishTimeMin": 80,
        "finishTimeMax": 84,
        "corners": [
          {
            "start": 650,
            "length": 200
          },
          {
            "start": 850,
            "length": 192
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 650
          },
          {
            "start": 1042,
            "end": 1400
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
        "finishTimeMax": 95,
        "corners": [
          {
            "start": 550,
            "length": 200
          },
          {
            "start": 750,
            "length": 192
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 550
          },
          {
            "start": 942,
            "end": 1600
          }
        ],
        "slopes": [
          {
            "start": 250,
            "length": 350,
            "slope": 10000
          },
          {
            "start": 600,
            "length": 300,
            "slope": -15000
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
        "finishTimeMax": 110,
        "corners": [
          {
            "start": 750,
            "length": 200
          },
          {
            "start": 950,
            "length": 192
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 750
          },
          {
            "start": 1142,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 450,
            "length": 350,
            "slope": 10000
          },
          {
            "start": 800,
            "length": 300,
            "slope": -15000
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 420,
            "length": 200
          },
          {
            "start": 620,
            "length": 200
          },
          {
            "start": 1250,
            "length": 200
          },
          {
            "start": 1450,
            "length": 192
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 420
          },
          {
            "start": 820,
            "end": 1250
          },
          {
            "start": 1642,
            "end": 2000
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 950,
            "length": 200
          },
          {
            "start": 1150,
            "length": 192
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 950
          },
          {
            "start": 1342,
            "end": 2000
          }
        ],
        "slopes": [
          {
            "start": 1000,
            "length": 300,
            "slope": -15000
          },
          {
            "start": 650,
            "length": 350,
            "slope": 10000
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
        "finishTimeMax": 135,
        "corners": [
          {
            "start": 650,
            "length": 200
          },
          {
            "start": 850,
            "length": 200
          },
          {
            "start": 1450,
            "length": 200
          },
          {
            "start": 1650,
            "length": 192
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 650
          },
          {
            "start": 1050,
            "end": 1450
          },
          {
            "start": 1842,
            "end": 2200
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
        "finishTimeMax": 149,
        "corners": [
          {
            "start": 810,
            "length": 200
          },
          {
            "start": 1010,
            "length": 200
          },
          {
            "start": 1650,
            "length": 200
          },
          {
            "start": 1850,
            "length": 192
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 810
          },
          {
            "start": 1210,
            "end": 1650
          },
          {
            "start": 2042,
            "end": 2400
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
        "finishTimeMin": 69,
        "finishTimeMax": 77,
        "corners": [
          {
            "start": 540,
            "length": 160
          },
          {
            "start": 700,
            "length": 147
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 540
          },
          {
            "start": 847,
            "end": 1200
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
        "finishTimeMax": 118,
        "corners": [
          {
            "start": 400,
            "length": 160
          },
          {
            "start": 560,
            "length": 160
          },
          {
            "start": 1140,
            "length": 160
          },
          {
            "start": 1300,
            "length": 147
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 400
          },
          {
            "start": 720,
            "end": 1140
          },
          {
            "start": 1447,
            "end": 1800
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
        "finishTimeMax": 164,
        "corners": [
          {
            "start": 380,
            "length": 160
          },
          {
            "start": 540,
            "length": 160
          },
          {
            "start": 1120,
            "length": 160
          },
          {
            "start": 1280,
            "length": 160
          },
          {
            "start": 1850,
            "length": 160
          },
          {
            "start": 2010,
            "length": 160
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 380
          },
          {
            "start": 700,
            "end": 1120
          },
          {
            "start": 1440,
            "end": 1850
          },
          {
            "start": 2170,
            "end": 2500
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
        "finishTimeMax": 71,
        "corners": [
          {
            "start": 420,
            "length": 300
          },
          {
            "start": 720,
            "length": 188
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 420
          },
          {
            "start": 908,
            "end": 1200
          }
        ],
        "slopes": [
          {
            "start": 180,
            "length": 100,
            "slope": 15000
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
        "finishTimeMax": 110,
        "corners": [
          {
            "start": 330,
            "length": 200
          },
          {
            "start": 530,
            "length": 200
          },
          {
            "start": 1020,
            "length": 300
          },
          {
            "start": 1320,
            "length": 188
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 330
          },
          {
            "start": 730,
            "end": 1020
          },
          {
            "start": 1508,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 780,
            "length": 100,
            "slope": 15000
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 530,
            "length": 200
          },
          {
            "start": 730,
            "length": 200
          },
          {
            "start": 1220,
            "length": 300
          },
          {
            "start": 1520,
            "length": 188
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 530
          },
          {
            "start": 930,
            "end": 1220
          },
          {
            "start": 1708,
            "end": 2000
          }
        ],
        "slopes": [
          {
            "start": 980,
            "length": 100,
            "slope": 15000
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
        "finishTimeMax": 165,
        "corners": [
          {
            "start": 220,
            "length": 300
          },
          {
            "start": 520,
            "length": 200
          },
          {
            "start": 1130,
            "length": 200
          },
          {
            "start": 1330,
            "length": 200
          },
          {
            "start": 1820,
            "length": 300
          },
          {
            "start": 2120,
            "length": 188
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 220
          },
          {
            "start": 720,
            "end": 1130
          },
          {
            "start": 1530,
            "end": 1820
          },
          {
            "start": 2308,
            "end": 2600
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 80,
            "slope": 15000
          },
          {
            "start": 1580,
            "length": 100,
            "slope": 15000
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
        "finishTimeMax": 72,
        "corners": [
          {
            "start": 500,
            "length": 210
          },
          {
            "start": 710,
            "length": 145
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 500
          },
          {
            "start": 855,
            "end": 1150
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
        "finishTimeMax": 113,
        "corners": [
          {
            "start": 360,
            "length": 170
          },
          {
            "start": 530,
            "length": 170
          },
          {
            "start": 1050,
            "length": 210
          },
          {
            "start": 1260,
            "length": 145
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 360
          },
          {
            "start": 700,
            "end": 1050
          },
          {
            "start": 1405,
            "end": 1700
          }
        ],
        "slopes": [
          {
            "start": 285,
            "length": 320,
            "slope": -10000
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
        "finishTimeMax": 156,
        "corners": [
          {
            "start": 310,
            "length": 210
          },
          {
            "start": 520,
            "length": 160
          },
          {
            "start": 1060,
            "length": 170
          },
          {
            "start": 1230,
            "length": 170
          },
          {
            "start": 1750,
            "length": 210
          },
          {
            "start": 1960,
            "length": 145
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 310
          },
          {
            "start": 680,
            "end": 1060
          },
          {
            "start": 1400,
            "end": 1750
          },
          {
            "start": 2105,
            "end": 2400
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
        "finishTimeMax": 71,
        "corners": [
          {
            "start": 300,
            "length": 350
          },
          {
            "start": 650,
            "length": 240
          }
        ],
        "straights": [
          {
            "start": 890,
            "end": 1200
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 200,
            "slope": -15000
          },
          {
            "start": 1025,
            "length": 110,
            "slope": 20000
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
        "finishTimeMax": 95,
        "corners": [
          {
            "start": 50,
            "length": 450
          },
          {
            "start": 700,
            "length": 350
          },
          {
            "start": 1050,
            "length": 240
          }
        ],
        "straights": [
          {
            "start": 1290,
            "end": 1600
          }
        ],
        "slopes": [
          {
            "start": 300,
            "length": 300,
            "slope": -15000
          },
          {
            "start": 1425,
            "length": 110,
            "slope": 20000
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
        "finishTimeMax": 110,
        "corners": [
          {
            "start": 175,
            "length": 250
          },
          {
            "start": 425,
            "length": 250
          },
          {
            "start": 1000,
            "length": 250
          },
          {
            "start": 1250,
            "length": 240
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 175
          },
          {
            "start": 675,
            "end": 1000
          },
          {
            "start": 1490,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 1,
            "length": 35,
            "slope": 20000
          },
          {
            "start": 125,
            "length": 200,
            "slope": 15000
          },
          {
            "start": 425,
            "length": 400,
            "slope": -15000
          },
          {
            "start": 1625,
            "length": 110,
            "slope": 20000
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 375,
            "length": 250
          },
          {
            "start": 625,
            "length": 250
          },
          {
            "start": 1200,
            "length": 250
          },
          {
            "start": 1450,
            "length": 240
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 375
          },
          {
            "start": 875,
            "end": 1200
          },
          {
            "start": 1690,
            "end": 2000
          }
        ],
        "slopes": [
          {
            "start": 325,
            "length": 200,
            "slope": 15000
          },
          {
            "start": 125,
            "length": 110,
            "slope": 20000
          },
          {
            "start": 625,
            "length": 400,
            "slope": -15000
          },
          {
            "start": 1825,
            "length": 110,
            "slope": 20000
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
        "finishTimeMax": 135,
        "corners": [
          {
            "start": 403,
            "length": 247
          },
          {
            "start": 650,
            "length": 450
          },
          {
            "start": 1300,
            "length": 350
          },
          {
            "start": 1650,
            "length": 240
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 403
          },
          {
            "start": 1890,
            "end": 2200
          }
        ],
        "slopes": [
          {
            "start": 153,
            "length": 110,
            "slope": 20000
          },
          {
            "start": 353,
            "length": 200,
            "slope": 15000
          },
          {
            "start": 900,
            "length": 300,
            "slope": -15000
          },
          {
            "start": 2025,
            "length": 110,
            "slope": 20000
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
        "finishTimeMax": 157,
        "corners": [
          {
            "start": 100,
            "length": 146
          },
          {
            "start": 246,
            "length": 250
          },
          {
            "start": 875,
            "length": 250
          },
          {
            "start": 1125,
            "length": 250
          },
          {
            "start": 1700,
            "length": 250
          },
          {
            "start": 1950,
            "length": 240
          }
        ],
        "straights": [
          {
            "start": 496,
            "end": 875
          },
          {
            "start": 1375,
            "end": 1700
          },
          {
            "start": 2190,
            "end": 2500
          }
        ],
        "slopes": [
          {
            "start": 621,
            "length": 110,
            "slope": 20000
          },
          {
            "start": 825,
            "length": 200,
            "slope": 15000
          },
          {
            "start": 1125,
            "length": 400,
            "slope": -15000
          },
          {
            "start": 2325,
            "length": 110,
            "slope": 20000
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
        "finishTimeMax": 227,
        "corners": [
          {
            "start": 290,
            "length": 250
          },
          {
            "start": 540,
            "length": 250
          },
          {
            "start": 1115,
            "length": 250
          },
          {
            "start": 1365,
            "length": 250
          },
          {
            "start": 1975,
            "length": 250
          },
          {
            "start": 2225,
            "length": 250
          },
          {
            "start": 2800,
            "length": 250
          },
          {
            "start": 3050,
            "length": 240
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 290
          },
          {
            "start": 790,
            "end": 1115
          },
          {
            "start": 1615,
            "end": 1975
          },
          {
            "start": 2475,
            "end": 2800
          },
          {
            "start": 3290,
            "end": 3600
          }
        ],
        "slopes": [
          {
            "start": 40,
            "length": 110,
            "slope": 20000
          },
          {
            "start": 240,
            "length": 200,
            "slope": 15000
          },
          {
            "start": 540,
            "length": 400,
            "slope": -15000
          },
          {
            "start": 1740,
            "length": 110,
            "slope": 20000
          },
          {
            "start": 1925,
            "length": 200,
            "slope": 15000
          },
          {
            "start": 2225,
            "length": 400,
            "slope": -15000
          },
          {
            "start": 3425,
            "length": 110,
            "slope": 20000
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
        "finishTimeMin": 69,
        "finishTimeMax": 77,
        "corners": [
          {
            "start": 500,
            "length": 200
          },
          {
            "start": 700,
            "length": 192
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 500
          },
          {
            "start": 892,
            "end": 1200
          }
        ],
        "slopes": [
          {
            "start": 175,
            "length": 175,
            "slope": -15000
          },
          {
            "start": 1000,
            "length": 175,
            "slope": 15000
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
        "finishTimeMax": 118,
        "corners": [
          {
            "start": 350,
            "length": 200
          },
          {
            "start": 550,
            "length": 200
          },
          {
            "start": 1100,
            "length": 200
          },
          {
            "start": 1300,
            "length": 192
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 350
          },
          {
            "start": 750,
            "end": 1100
          },
          {
            "start": 1492,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 100,
            "length": 175,
            "slope": 15000
          },
          {
            "start": 350,
            "length": 175,
            "slope": 10000
          },
          {
            "start": 775,
            "length": 175,
            "slope": -15000
          },
          {
            "start": 1600,
            "length": 175,
            "slope": 15000
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
        "finishTimeMax": 156,
        "corners": [
          {
            "start": 200,
            "length": 200
          },
          {
            "start": 400,
            "length": 200
          },
          {
            "start": 950,
            "length": 200
          },
          {
            "start": 1150,
            "length": 200
          },
          {
            "start": 1700,
            "length": 200
          },
          {
            "start": 1900,
            "length": 192
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 200
          },
          {
            "start": 600,
            "end": 950
          },
          {
            "start": 1350,
            "end": 1700
          },
          {
            "start": 2092,
            "end": 2400
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
        "finishTimeMax": 164,
        "corners": [
          {
            "start": 300,
            "length": 200
          },
          {
            "start": 500,
            "length": 200
          },
          {
            "start": 1050,
            "length": 200
          },
          {
            "start": 1250,
            "length": 200
          },
          {
            "start": 1800,
            "length": 200
          },
          {
            "start": 2000,
            "length": 192
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 300
          },
          {
            "start": 700,
            "end": 1050
          },
          {
            "start": 1450,
            "end": 1800
          },
          {
            "start": 2192,
            "end": 2500
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
        "finishTimeMin": 80,
        "finishTimeMax": 84,
        "corners": [
          {
            "start": 350,
            "length": 275
          },
          {
            "start": 625,
            "length": 250
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 350
          },
          {
            "start": 875,
            "end": 1400
          }
        ],
        "slopes": [
          {
            "start": 125,
            "length": 75,
            "slope": 20000
          },
          {
            "start": 250,
            "length": 250,
            "slope": -15000
          },
          {
            "start": 950,
            "length": 150,
            "slope": 15000
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
        "finishTimeMax": 95,
        "corners": [
          {
            "start": 550,
            "length": 275
          },
          {
            "start": 825,
            "length": 250
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 550
          },
          {
            "start": 1075,
            "end": 1600
          }
        ],
        "slopes": [
          {
            "start": 450,
            "length": 250,
            "slope": -15000
          },
          {
            "start": 325,
            "length": 75,
            "slope": 20000
          },
          {
            "start": 1150,
            "length": 150,
            "slope": 15000
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
        "finishTimeMax": 110,
        "corners": [
          {
            "start": 75,
            "length": 250
          },
          {
            "start": 750,
            "length": 275
          },
          {
            "start": 1025,
            "length": 250
          }
        ],
        "straights": [
          {
            "start": 325,
            "end": 750
          },
          {
            "start": 1275,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 525,
            "length": 75,
            "slope": 20000
          },
          {
            "start": 650,
            "length": 250,
            "slope": -15000
          },
          {
            "start": 1350,
            "length": 150,
            "slope": 15000
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 200,
            "length": 200
          },
          {
            "start": 950,
            "length": 275
          },
          {
            "start": 1225,
            "length": 250
          }
        ],
        "straights": [
          {
            "start": 400,
            "end": 950
          },
          {
            "start": 1475,
            "end": 2000
          }
        ],
        "slopes": [
          {
            "start": 725,
            "length": 75,
            "slope": 20000
          },
          {
            "start": 850,
            "length": 250,
            "slope": -15000
          },
          {
            "start": 1550,
            "length": 150,
            "slope": 15000
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
        "finishTimeMax": 143,
        "corners": [
          {
            "start": 225,
            "length": 250
          },
          {
            "start": 475,
            "length": 325
          },
          {
            "start": 1250,
            "length": 275
          },
          {
            "start": 1525,
            "length": 250
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 225
          },
          {
            "start": 800,
            "end": 1250
          },
          {
            "start": 1775,
            "end": 2300
          }
        ],
        "slopes": [
          {
            "start": 1025,
            "length": 75,
            "slope": 20000
          },
          {
            "start": 1150,
            "length": 250,
            "slope": -15000
          },
          {
            "start": 1850,
            "length": 150,
            "slope": 15000
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
        "finishTimeMax": 149,
        "corners": [
          {
            "start": 325,
            "length": 250
          },
          {
            "start": 575,
            "length": 325
          },
          {
            "start": 1350,
            "length": 275
          },
          {
            "start": 1625,
            "length": 250
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 325
          },
          {
            "start": 900,
            "end": 1350
          },
          {
            "start": 1875,
            "end": 2400
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 40,
            "slope": 15000
          },
          {
            "start": 1125,
            "length": 75,
            "slope": 20000
          },
          {
            "start": 1250,
            "length": 250,
            "slope": -15000
          },
          {
            "start": 1950,
            "length": 150,
            "slope": 15000
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
        "finishTimeMax": 157,
        "corners": [
          {
            "start": 425,
            "length": 250
          },
          {
            "start": 675,
            "length": 325
          },
          {
            "start": 1450,
            "length": 275
          },
          {
            "start": 1725,
            "length": 250
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 425
          },
          {
            "start": 1000,
            "end": 1450
          },
          {
            "start": 1975,
            "end": 2500
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 140,
            "slope": 15000
          },
          {
            "start": 1225,
            "length": 75,
            "slope": 20000
          },
          {
            "start": 1350,
            "length": 250,
            "slope": -15000
          },
          {
            "start": 2050,
            "length": 150,
            "slope": 15000
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
        "finishTimeMax": 214,
        "corners": [
          {
            "start": 292,
            "length": 275
          },
          {
            "start": 567,
            "length": 275
          },
          {
            "start": 1325,
            "length": 250
          },
          {
            "start": 1575,
            "length": 325
          },
          {
            "start": 2350,
            "length": 275
          },
          {
            "start": 2625,
            "length": 250
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 292
          },
          {
            "start": 842,
            "end": 1325
          },
          {
            "start": 1900,
            "end": 2350
          },
          {
            "start": 2875,
            "end": 3400
          }
        ],
        "slopes": [
          {
            "start": 67,
            "length": 75,
            "slope": 20000
          },
          {
            "start": 192,
            "length": 250,
            "slope": -15000
          },
          {
            "start": 892,
            "length": 150,
            "slope": 15000
          },
          {
            "start": 2125,
            "length": 75,
            "slope": 20000
          },
          {
            "start": 2250,
            "length": 250,
            "slope": -15000
          },
          {
            "start": 2950,
            "length": 150,
            "slope": 15000
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
        "finishTimeMax": 82,
        "corners": [
          {
            "start": 350,
            "length": 225
          },
          {
            "start": 575,
            "length": 224
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 350
          },
          {
            "start": 799,
            "end": 1300
          }
        ],
        "slopes": [
          {
            "start": 275,
            "length": 200,
            "slope": -10000
          },
          {
            "start": 800,
            "length": 250,
            "slope": 15000
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
        "finishTimeMax": 94,
        "corners": [
          {
            "start": 450,
            "length": 225
          },
          {
            "start": 675,
            "length": 224
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 450
          },
          {
            "start": 899,
            "end": 1400
          }
        ],
        "slopes": [
          {
            "start": 375,
            "length": 200,
            "slope": -10000
          },
          {
            "start": 900,
            "length": 250,
            "slope": 15000
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
        "finishTimeMax": 108,
        "corners": [
          {
            "start": 650,
            "length": 225
          },
          {
            "start": 875,
            "length": 224
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 650
          },
          {
            "start": 1099,
            "end": 1600
          }
        ],
        "slopes": [
          {
            "start": 575,
            "length": 200,
            "slope": -10000
          },
          {
            "start": 1100,
            "length": 250,
            "slope": 15000
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
        "finishTimeMax": 133,
        "corners": [
          {
            "start": 200,
            "length": 250
          },
          {
            "start": 450,
            "length": 250
          },
          {
            "start": 1150,
            "length": 225
          },
          {
            "start": 1375,
            "length": 224
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 200
          },
          {
            "start": 700,
            "end": 1150
          },
          {
            "start": 1599,
            "end": 2100
          }
        ],
        "slopes": [
          {
            "start": 1075,
            "length": 200,
            "slope": -10000
          },
          {
            "start": 1600,
            "length": 250,
            "slope": 15000
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
        "finishTimeMax": 156,
        "corners": [
          {
            "start": 500,
            "length": 250
          },
          {
            "start": 750,
            "length": 250
          },
          {
            "start": 1450,
            "length": 225
          },
          {
            "start": 1675,
            "length": 225
          },
          {
            "start": 2348,
            "length": 500
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 500
          },
          {
            "start": 1000,
            "end": 1450
          },
          {
            "start": 1900,
            "end": 2400
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
        "finishTimeMax": 71,
        "corners": [
          {
            "start": 300,
            "length": 250
          },
          {
            "start": 550,
            "length": 238
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 300
          },
          {
            "start": 788,
            "end": 1200
          }
        ],
        "slopes": [
          {
            "start": 100,
            "length": 775,
            "slope": -10000
          },
          {
            "start": 875,
            "length": 100,
            "slope": 20000
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
        "finishTimeMin": 80,
        "finishTimeMax": 84,
        "corners": [
          {
            "start": 500,
            "length": 250
          },
          {
            "start": 750,
            "length": 238
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 500
          },
          {
            "start": 988,
            "end": 1400
          }
        ],
        "slopes": [
          {
            "start": 300,
            "length": 775,
            "slope": -10000
          },
          {
            "start": 1075,
            "length": 100,
            "slope": 20000
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
        "finishTimeMax": 95,
        "corners": [
          {
            "start": 150,
            "length": 150
          },
          {
            "start": 700,
            "length": 250
          },
          {
            "start": 950,
            "length": 238
          }
        ],
        "straights": [
          {
            "start": 300,
            "end": 700
          },
          {
            "start": 1188,
            "end": 1600
          }
        ],
        "slopes": [
          {
            "start": 500,
            "length": 775,
            "slope": -10000
          },
          {
            "start": 1275,
            "length": 100,
            "slope": 20000
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 300,
            "length": 200
          },
          {
            "start": 500,
            "length": 200
          },
          {
            "start": 1100,
            "length": 250
          },
          {
            "start": 1350,
            "length": 238
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 300
          },
          {
            "start": 700,
            "end": 1100
          },
          {
            "start": 1588,
            "end": 2000
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 50,
            "slope": 20000
          },
          {
            "start": 900,
            "length": 775,
            "slope": -10000
          },
          {
            "start": 1675,
            "length": 100,
            "slope": 20000
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
        "finishTimeMax": 135,
        "corners": [
          {
            "start": 500,
            "length": 200
          },
          {
            "start": 700,
            "length": 200
          },
          {
            "start": 1300,
            "length": 250
          },
          {
            "start": 1550,
            "length": 238
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 500
          },
          {
            "start": 900,
            "end": 1300
          },
          {
            "start": 1788,
            "end": 2200
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 150,
            "slope": -10000
          },
          {
            "start": 150,
            "length": 100,
            "slope": 20000
          },
          {
            "start": 1100,
            "length": 775,
            "slope": -10000
          },
          {
            "start": 1875,
            "length": 100,
            "slope": 20000
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
        "finishTimeMin": 69,
        "finishTimeMax": 77,
        "corners": [
          {
            "start": 400,
            "length": 200
          },
          {
            "start": 600,
            "length": 190
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 400
          },
          {
            "start": 790,
            "end": 1200
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
        "finishTimeMax": 94,
        "corners": [
          {
            "start": 600,
            "length": 200
          },
          {
            "start": 800,
            "length": 190
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 600
          },
          {
            "start": 990,
            "end": 1400
          }
        ],
        "slopes": [
          {
            "start": 425,
            "length": 600,
            "slope": -15000
          },
          {
            "start": 1025,
            "length": 150,
            "slope": 15000
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
        "finishTimeMax": 118,
        "corners": [
          {
            "start": 270,
            "length": 165
          },
          {
            "start": 435,
            "length": 165
          },
          {
            "start": 1000,
            "length": 200
          },
          {
            "start": 1200,
            "length": 190
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 270
          },
          {
            "start": 600,
            "end": 1000
          },
          {
            "start": 1390,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 50,
            "slope": 15000
          },
          {
            "start": 825,
            "length": 600,
            "slope": -15000
          },
          {
            "start": 1425,
            "length": 150,
            "slope": 15000
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 370,
            "length": 165
          },
          {
            "start": 535,
            "length": 165
          },
          {
            "start": 1100,
            "length": 200
          },
          {
            "start": 1300,
            "length": 190
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 370
          },
          {
            "start": 700,
            "end": 1100
          },
          {
            "start": 1490,
            "end": 1900
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
        "finishTimeMax": 71,
        "corners": [
          {
            "start": 320,
            "length": 275
          },
          {
            "start": 595,
            "length": 277
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 320
          },
          {
            "start": 872,
            "end": 1200
          }
        ],
        "slopes": [
          {
            "start": 120,
            "length": 175,
            "slope": 15000
          },
          {
            "start": 420,
            "length": 150,
            "slope": -15000
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
        "finishTimeMin": 80,
        "finishTimeMax": 84,
        "corners": [
          {
            "start": 520,
            "length": 275
          },
          {
            "start": 795,
            "length": 277
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 520
          },
          {
            "start": 1072,
            "end": 1400
          }
        ],
        "slopes": [
          {
            "start": 320,
            "length": 175,
            "slope": 15000
          },
          {
            "start": 620,
            "length": 150,
            "slope": -15000
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
        "finishTimeMin": 80,
        "finishTimeMax": 84,
        "corners": [
          {
            "start": 500,
            "length": 250
          },
          {
            "start": 750,
            "length": 247
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 500
          },
          {
            "start": 997,
            "end": 1400
          }
        ],
        "slopes": [
          {
            "start": 250,
            "length": 100,
            "slope": 20000
          },
          {
            "start": 350,
            "length": 225,
            "slope": 10000
          },
          {
            "start": 575,
            "length": 150,
            "slope": -20000
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
        "finishTimeMax": 95,
        "corners": [
          {
            "start": 720,
            "length": 275
          },
          {
            "start": 995,
            "length": 277
          }
        ],
        "straights": [
          {
            "start": 200,
            "end": 720
          },
          {
            "start": 1272,
            "end": 1600
          }
        ],
        "slopes": [
          {
            "start": 520,
            "length": 175,
            "slope": 15000
          },
          {
            "start": 820,
            "length": 150,
            "slope": -15000
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
        "finishTimeMax": 95,
        "corners": [
          {
            "start": 700,
            "length": 250
          },
          {
            "start": 950,
            "length": 247
          }
        ],
        "straights": [
          {
            "start": 200,
            "end": 700
          },
          {
            "start": 1197,
            "end": 1600
          }
        ],
        "slopes": [
          {
            "start": 450,
            "length": 100,
            "slope": 20000
          },
          {
            "start": 550,
            "length": 225,
            "slope": 10000
          },
          {
            "start": 775,
            "length": 150,
            "slope": -20000
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
        "finishTimeMax": 110,
        "corners": [
          {
            "start": 900,
            "length": 250
          },
          {
            "start": 1150,
            "length": 247
          }
        ],
        "straights": [
          {
            "start": 400,
            "end": 900
          },
          {
            "start": 1397,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 650,
            "length": 100,
            "slope": 20000
          },
          {
            "start": 750,
            "length": 225,
            "slope": 10000
          },
          {
            "start": 975,
            "length": 150,
            "slope": -20000
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 400,
            "length": 185
          },
          {
            "start": 585,
            "length": 185
          },
          {
            "start": 1120,
            "length": 275
          },
          {
            "start": 1395,
            "length": 277
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 400
          },
          {
            "start": 770,
            "end": 1120
          },
          {
            "start": 1672,
            "end": 2000
          }
        ],
        "slopes": [
          {
            "start": 970,
            "length": 175,
            "slope": 15000
          },
          {
            "start": 1270,
            "length": 150,
            "slope": -15000
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
        "finishTimeMax": 135,
        "corners": [
          {
            "start": 400,
            "length": 200
          },
          {
            "start": 600,
            "length": 200
          },
          {
            "start": 1300,
            "length": 250
          },
          {
            "start": 1550,
            "length": 247
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 400
          },
          {
            "start": 800,
            "end": 1300
          },
          {
            "start": 1797,
            "end": 2200
          }
        ],
        "slopes": [
          {
            "start": 1050,
            "length": 100,
            "slope": 20000
          },
          {
            "start": 1150,
            "length": 225,
            "slope": 10000
          },
          {
            "start": 1375,
            "length": 150,
            "slope": -20000
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
        "finishTimeMax": 149,
        "corners": [
          {
            "start": 600,
            "length": 200
          },
          {
            "start": 800,
            "length": 200
          },
          {
            "start": 1500,
            "length": 250
          },
          {
            "start": 1750,
            "length": 247
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 600
          },
          {
            "start": 1000,
            "end": 1500
          },
          {
            "start": 1997,
            "end": 2400
          }
        ],
        "slopes": [
          {
            "start": 1350,
            "length": 225,
            "slope": 10000
          },
          {
            "start": 1250,
            "length": 100,
            "slope": 20000
          },
          {
            "start": 1575,
            "length": 150,
            "slope": -20000
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
        "finishTimeMax": 190,
        "corners": [
          {
            "start": 261,
            "length": 250
          },
          {
            "start": 511,
            "length": 250
          },
          {
            "start": 1250,
            "length": 200
          },
          {
            "start": 1450,
            "length": 200
          },
          {
            "start": 2100,
            "length": 250
          },
          {
            "start": 2350,
            "length": 247
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 261
          },
          {
            "start": 761,
            "end": 1250
          },
          {
            "start": 1650,
            "end": 2100
          },
          {
            "start": 2597,
            "end": 3000
          }
        ],
        "slopes": [
          {
            "start": 11,
            "length": 100,
            "slope": 20000
          },
          {
            "start": 111,
            "length": 225,
            "slope": 10000
          },
          {
            "start": 336,
            "length": 150,
            "slope": -20000
          },
          {
            "start": 1950,
            "length": 225,
            "slope": 10000
          },
          {
            "start": 1850,
            "length": 100,
            "slope": 20000
          },
          {
            "start": 2175,
            "length": 150,
            "slope": -20000
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
        "finishTimeMin": 193,
        "finishTimeMax": 204,
        "corners": [
          {
            "start": 458,
            "length": 250
          },
          {
            "start": 708,
            "length": 250
          },
          {
            "start": 1450,
            "length": 200
          },
          {
            "start": 1650,
            "length": 200
          },
          {
            "start": 2300,
            "length": 250
          },
          {
            "start": 2550,
            "length": 247
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 458
          },
          {
            "start": 958,
            "end": 1450
          },
          {
            "start": 1850,
            "end": 2300
          },
          {
            "start": 2797,
            "end": 3200
          }
        ],
        "slopes": [
          {
            "start": 208,
            "length": 100,
            "slope": 20000
          },
          {
            "start": 308,
            "length": 225,
            "slope": 10000
          },
          {
            "start": 533,
            "length": 150,
            "slope": -20000
          },
          {
            "start": 2050,
            "length": 100,
            "slope": 20000
          },
          {
            "start": 2375,
            "length": 150,
            "slope": -20000
          },
          {
            "start": 2150,
            "length": 225,
            "slope": 10000
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
        "finishTimeMin": 69,
        "finishTimeMax": 77,
        "corners": [
          {
            "start": 400,
            "length": 225
          },
          {
            "start": 625,
            "length": 246
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 400
          },
          {
            "start": 871,
            "end": 1200
          }
        ],
        "slopes": [
          {
            "start": 175,
            "length": 200,
            "slope": 15000
          },
          {
            "start": 475,
            "length": 200,
            "slope": -15000
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
        "finishTimeMax": 94,
        "corners": [
          {
            "start": 600,
            "length": 225
          },
          {
            "start": 825,
            "length": 246
          }
        ],
        "straights": [
          {
            "start": 100,
            "end": 600
          },
          {
            "start": 1071,
            "end": 1400
          }
        ],
        "slopes": [
          {
            "start": 375,
            "length": 200,
            "slope": 15000
          },
          {
            "start": 675,
            "length": 200,
            "slope": -15000
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
        "finishTimeMax": 118,
        "corners": [
          {
            "start": 300,
            "length": 150
          },
          {
            "start": 450,
            "length": 150
          },
          {
            "start": 1000,
            "length": 225
          },
          {
            "start": 1225,
            "length": 246
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 300
          },
          {
            "start": 600,
            "end": 1000
          },
          {
            "start": 1471,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 775,
            "length": 200,
            "slope": 15000
          },
          {
            "start": 1075,
            "length": 200,
            "slope": -15000
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 400,
            "length": 150
          },
          {
            "start": 550,
            "length": 150
          },
          {
            "start": 1100,
            "length": 225
          },
          {
            "start": 1325,
            "length": 246
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 400
          },
          {
            "start": 700,
            "end": 1100
          },
          {
            "start": 1571,
            "end": 1900
          }
        ],
        "slopes": [
          {
            "start": 875,
            "length": 200,
            "slope": 15000
          },
          {
            "start": 1175,
            "length": 200,
            "slope": -15000
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
        "finishTimeMax": 71,
        "corners": [
          {
            "start": 250,
            "length": 300
          },
          {
            "start": 550,
            "length": 294
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 250
          },
          {
            "start": 844,
            "end": 1200
          }
        ],
        "slopes": [
          {
            "start": 400,
            "length": 595,
            "slope": -10000
          },
          {
            "start": 1000,
            "length": 125,
            "slope": 20000
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
        "finishTimeMin": 80,
        "finishTimeMax": 84,
        "corners": [
          {
            "start": 450,
            "length": 300
          },
          {
            "start": 750,
            "length": 294
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 450
          },
          {
            "start": 1044,
            "end": 1400
          }
        ],
        "slopes": [
          {
            "start": 600,
            "length": 595,
            "slope": -10000
          },
          {
            "start": 1200,
            "length": 125,
            "slope": 20000
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
        "finishTimeMax": 95,
        "corners": [
          {
            "start": 450,
            "length": 350
          },
          {
            "start": 800,
            "length": 327
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 450
          },
          {
            "start": 1127,
            "end": 1600
          }
        ],
        "slopes": [
          {
            "start": 950,
            "length": 400,
            "slope": -10000
          },
          {
            "start": 1405,
            "length": 120,
            "slope": 20000
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
        "finishTimeMax": 110,
        "corners": [
          {
            "start": 650,
            "length": 350
          },
          {
            "start": 1000,
            "length": 327
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 650
          },
          {
            "start": 1327,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 1150,
            "length": 400,
            "slope": -10000
          },
          {
            "start": 1605,
            "length": 120,
            "slope": 20000
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 370,
            "length": 190
          },
          {
            "start": 560,
            "length": 190
          },
          {
            "start": 1050,
            "length": 300
          },
          {
            "start": 1350,
            "length": 294
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 370
          },
          {
            "start": 750,
            "end": 1050
          },
          {
            "start": 1644,
            "end": 2000
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 140,
            "slope": -10000
          },
          {
            "start": 145,
            "length": 125,
            "slope": 20000
          },
          {
            "start": 1200,
            "length": 595,
            "slope": -10000
          },
          {
            "start": 1800,
            "length": 125,
            "slope": 20000
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
        "finishTimeMax": 135,
        "corners": [
          {
            "start": 520,
            "length": 190
          },
          {
            "start": 710,
            "length": 190
          },
          {
            "start": 1250,
            "length": 300
          },
          {
            "start": 1550,
            "length": 294
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 520
          },
          {
            "start": 900,
            "end": 1250
          },
          {
            "start": 1844,
            "end": 2200
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 290,
            "slope": -10000
          },
          {
            "start": 295,
            "length": 125,
            "slope": 20000
          },
          {
            "start": 1400,
            "length": 595,
            "slope": -10000
          },
          {
            "start": 2000,
            "length": 125,
            "slope": 20000
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
        "finishTimeMax": 149,
        "corners": [
          {
            "start": 382,
            "length": 190
          },
          {
            "start": 558,
            "length": 190
          },
          {
            "start": 1250,
            "length": 350
          },
          {
            "start": 1600,
            "length": 327
          }
        ],
        "straights": [
          {
            "start": 750,
            "end": 1250
          },
          {
            "start": 1927,
            "end": 2400
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 132,
            "slope": -10000
          },
          {
            "start": 187,
            "length": 120,
            "slope": 20000
          },
          {
            "start": 1750,
            "length": 400,
            "slope": -10000
          },
          {
            "start": 2205,
            "length": 120,
            "slope": 20000
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
        "finishTimeMax": 165,
        "corners": [
          {
            "start": 570,
            "length": 190
          },
          {
            "start": 760,
            "length": 190
          },
          {
            "start": 1450,
            "length": 350
          },
          {
            "start": 1800,
            "length": 327
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 570
          },
          {
            "start": 950,
            "end": 1450
          },
          {
            "start": 2127,
            "end": 2600
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 315,
            "slope": -10000
          },
          {
            "start": 370,
            "length": 120,
            "slope": 20000
          },
          {
            "start": 1950,
            "length": 400,
            "slope": -10000
          },
          {
            "start": 2405,
            "length": 120,
            "slope": 20000
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
        "finishTimeMax": 190,
        "corners": [
          {
            "start": 348,
            "length": 300
          },
          {
            "start": 648,
            "length": 300
          },
          {
            "start": 1320,
            "length": 190
          },
          {
            "start": 1510,
            "length": 190
          },
          {
            "start": 2050,
            "length": 300
          },
          {
            "start": 2350,
            "length": 294
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 348
          },
          {
            "start": 948,
            "end": 1320
          },
          {
            "start": 1700,
            "end": 2050
          },
          {
            "start": 2644,
            "end": 3000
          }
        ],
        "slopes": [
          {
            "start": 498,
            "length": 595,
            "slope": -10000
          },
          {
            "start": 1095,
            "length": 125,
            "slope": 20000
          },
          {
            "start": 2200,
            "length": 595,
            "slope": -10000
          },
          {
            "start": 2800,
            "length": 125,
            "slope": 20000
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
        "finishTimeMin": 69,
        "finishTimeMax": 77,
        "corners": [
          {
            "start": 350,
            "length": 250
          },
          {
            "start": 600,
            "length": 248
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 350
          },
          {
            "start": 848,
            "end": 1200
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
        "finishTimeMax": 94,
        "corners": [
          {
            "start": 550,
            "length": 250
          },
          {
            "start": 800,
            "length": 248
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 550
          },
          {
            "start": 1048,
            "end": 1400
          }
        ],
        "slopes": [
          {
            "start": 1200,
            "length": 125,
            "slope": 15000
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
        "finishTimeMax": 118,
        "corners": [
          {
            "start": 330,
            "length": 150
          },
          {
            "start": 480,
            "length": 150
          },
          {
            "start": 950,
            "length": 250
          },
          {
            "start": 1200,
            "length": 248
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 330
          },
          {
            "start": 630,
            "end": 950
          },
          {
            "start": 1448,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 105,
            "length": 125,
            "slope": 15000
          },
          {
            "start": 1599,
            "length": 125,
            "slope": 15000
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
        "finishTimeMax": 129,
        "corners": [
          {
            "start": 500,
            "length": 150
          },
          {
            "start": 650,
            "length": 150
          },
          {
            "start": 1150,
            "length": 250
          },
          {
            "start": 1400,
            "length": 248
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 500
          },
          {
            "start": 800,
            "end": 1150
          },
          {
            "start": 1648,
            "end": 2000
          }
        ],
        "slopes": [
          {
            "start": 275,
            "length": 125,
            "slope": 15000
          },
          {
            "start": 1800,
            "length": 125,
            "slope": 15000
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
        "finishTimeMin": 193,
        "finishTimeMax": 204,
        "corners": [
          {
            "start": 370,
            "length": 350
          },
          {
            "start": 720,
            "length": 350
          },
          {
            "start": 1520,
            "length": 190
          },
          {
            "start": 1710,
            "length": 190
          },
          {
            "start": 2250,
            "length": 300
          },
          {
            "start": 2550,
            "length": 294
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 370
          },
          {
            "start": 1070,
            "end": 1520
          },
          {
            "start": 1900,
            "end": 2250
          },
          {
            "start": 2844,
            "end": 3200
          }
        ],
        "slopes": [
          {
            "start": 870,
            "length": 400,
            "slope": -10000
          },
          {
            "start": 1325,
            "length": 120,
            "slope": 20000
          },
          {
            "start": 2400,
            "length": 595,
            "slope": -10000
          },
          {
            "start": 3000,
            "length": 125,
            "slope": 20000
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
        "finishTimeMax": 71,
        "corners": [
          {
            "start": 500,
            "length": 205
          },
          {
            "start": 705,
            "length": 202
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 500
          },
          {
            "start": 907,
            "end": 1200
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 60,
            "slope": -15000
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
        "finishTimeMax": 110,
        "corners": [
          {
            "start": 290,
            "length": 205
          },
          {
            "start": 495,
            "length": 205
          },
          {
            "start": 1100,
            "length": 205
          },
          {
            "start": 1305,
            "length": 202
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 290
          },
          {
            "start": 700,
            "end": 1100
          },
          {
            "start": 1507,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 280,
            "length": 255,
            "slope": 15000
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
        "finishTimeMax": 123,
        "corners": [
          {
            "start": 490,
            "length": 205
          },
          {
            "start": 695,
            "length": 205
          },
          {
            "start": 1300,
            "length": 205
          },
          {
            "start": 1505,
            "length": 202
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 490
          },
          {
            "start": 900,
            "end": 1300
          },
          {
            "start": 1707,
            "end": 2000
          }
        ],
        "slopes": [
          {
            "start": 480,
            "length": 255,
            "slope": 15000
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
        "finishTimeMax": 165,
        "corners": [
          {
            "start": 309,
            "length": 205
          },
          {
            "start": 514,
            "length": 205
          },
          {
            "start": 1110,
            "length": 205
          },
          {
            "start": 1315,
            "length": 205
          },
          {
            "start": 1900,
            "length": 205
          },
          {
            "start": 2105,
            "length": 202
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 309
          },
          {
            "start": 719,
            "end": 1110
          },
          {
            "start": 1520,
            "end": 1900
          },
          {
            "start": 2307,
            "end": 2600
          }
        ],
        "slopes": [
          {
            "start": 1100,
            "length": 255,
            "slope": 15000
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
        "finishTimeMax": 63,
        "corners": [
          {
            "start": 360,
            "length": 180
          },
          {
            "start": 540,
            "length": 169
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 360
          },
          {
            "start": 709,
            "end": 1000
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
        "finishTimeMax": 113,
        "corners": [
          {
            "start": 340,
            "length": 180
          },
          {
            "start": 520,
            "length": 180
          },
          {
            "start": 1060,
            "length": 180
          },
          {
            "start": 1240,
            "length": 169
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 340
          },
          {
            "start": 700,
            "end": 1060
          },
          {
            "start": 1409,
            "end": 1700
          }
        ],
        "slopes": [
          {
            "start": 370,
            "length": 150,
            "slope": 15000
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
        "finishTimeMax": 156,
        "corners": [
          {
            "start": 312,
            "length": 180
          },
          {
            "start": 492,
            "length": 180
          },
          {
            "start": 1040,
            "length": 180
          },
          {
            "start": 1220,
            "length": 180
          },
          {
            "start": 1760,
            "length": 180
          },
          {
            "start": 1940,
            "length": 169
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 312
          },
          {
            "start": 672,
            "end": 1040
          },
          {
            "start": 1400,
            "end": 1760
          },
          {
            "start": 2109,
            "end": 2400
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
        "finishTimeMin": 69,
        "finishTimeMax": 77,
        "corners": [
          {
            "start": 500,
            "length": 150
          },
          {
            "start": 650,
            "length": 164
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 500
          },
          {
            "start": 814,
            "end": 1200
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
        "finishTimeMax": 118,
        "corners": [
          {
            "start": 300,
            "length": 150
          },
          {
            "start": 500,
            "length": 150
          },
          {
            "start": 1100,
            "length": 150
          },
          {
            "start": 1250,
            "length": 164
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 301
          },
          {
            "start": 600,
            "end": 1100.23
          },
          {
            "start": 1414,
            "end": 1800
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
        "finishTimeMax": 129,
        "corners": [
          {
            "start": 500,
            "length": 150
          },
          {
            "start": 650,
            "length": 150
          },
          {
            "start": 1300,
            "length": 150
          },
          {
            "start": 1450,
            "length": 164
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 500
          },
          {
            "start": 800,
            "end": 1300
          },
          {
            "start": 1614,
            "end": 2000
          }
        ],
        "slopes": []
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
        "finishTimeMin": 87,
        "finishTimeMax": 94,
        "corners": [
          {
            "start": 300,
            "length": 100
          },
          {
            "start": 400,
            "length": 100
          },
          {
            "start": 900,
            "length": 100
          },
          {
            "start": 1000,
            "length": 100
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 300
          },
          {
            "start": 500,
            "end": 900
          },
          {
            "start": 1100,
            "end": 1400
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
        "finishTimeMin": 98,
        "finishTimeMax": 108,
        "corners": [
          {
            "start": 500,
            "length": 100
          },
          {
            "start": 600,
            "length": 100
          },
          {
            "start": 1100,
            "length": 100
          },
          {
            "start": 1200,
            "length": 100
          }
        ],
        "straights": [
          {
            "start": 100,
            "end": 500
          },
          {
            "start": 700,
            "end": 1100
          },
          {
            "start": 1300,
            "end": 1600
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
        "finishTimeMin": 131,
        "finishTimeMax": 140,
        "corners": [
          {
            "start": 400,
            "length": 100
          },
          {
            "start": 500,
            "length": 100
          },
          {
            "start": 1000,
            "length": 100
          },
          {
            "start": 1100,
            "length": 100
          },
          {
            "start": 1600,
            "length": 100
          },
          {
            "start": 1700,
            "length": 100
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 400
          },
          {
            "start": 600,
            "end": 1000
          },
          {
            "start": 1200,
            "end": 1600
          },
          {
            "start": 1800,
            "end": 2100
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
        "finishTimeMin": 58,
        "finishTimeMax": 63,
        "corners": [
          {
            "start": 370,
            "length": 170
          },
          {
            "start": 540,
            "length": 152
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 370
          },
          {
            "start": 692,
            "end": 1000
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
        "finishTimeMin": 96,
        "finishTimeMax": 108,
        "corners": [
          {
            "start": 260,
            "length": 180
          },
          {
            "start": 440,
            "length": 160
          },
          {
            "start": 970,
            "length": 170
          },
          {
            "start": 1140,
            "length": 152
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 260
          },
          {
            "start": 600,
            "end": 970
          },
          {
            "start": 1292,
            "end": 1600
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
        "finishTimeMin": 108,
        "finishTimeMax": 118,
        "corners": [
          {
            "start": 460,
            "length": 180
          },
          {
            "start": 640,
            "length": 160
          },
          {
            "start": 1170,
            "length": 170
          },
          {
            "start": 1340,
            "length": 152
          }
        ],
        "straights": [
          {
            "start": 90,
            "end": 460
          },
          {
            "start": 800,
            "end": 1170
          },
          {
            "start": 1492,
            "end": 1800
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
        "finishTimeMin": 150,
        "finishTimeMax": 156,
        "corners": [
          {
            "start": 370,
            "length": 170
          },
          {
            "start": 540,
            "length": 150
          },
          {
            "start": 1060,
            "length": 180
          },
          {
            "start": 1240,
            "length": 160
          },
          {
            "start": 1770,
            "length": 170
          },
          {
            "start": 1940,
            "length": 152
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 370
          },
          {
            "start": 690,
            "end": 1060
          },
          {
            "start": 1400,
            "end": 1770
          },
          {
            "start": 2092,
            "end": 2400
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
        "finishTimeMin": 69,
        "finishTimeMax": 77,
        "corners": [
          {
            "start": 500,
            "length": 200
          },
          {
            "start": 700,
            "length": 200
          }
        ],
        "straights": [
          {
            "start": 100,
            "end": 500
          },
          {
            "start": 900,
            "end": 1200
          }
        ],
        "slopes": [
          {
            "start": 100,
            "length": 375,
            "slope": 10000
          },
          {
            "start": 475,
            "length": 450,
            "slope": -15000
          },
          {
            "start": 975,
            "length": 175,
            "slope": 10000
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
        "finishTimeMin": 93,
        "finishTimeMax": 108,
        "corners": [
          {
            "start": 900,
            "length": 200
          },
          {
            "start": 1100,
            "length": 200
          }
        ],
        "straights": [
          {
            "start": 500,
            "end": 900
          },
          {
            "start": 1300,
            "end": 1600
          }
        ],
        "slopes": [
          {
            "start": 500,
            "length": 375,
            "slope": 10000
          },
          {
            "start": 875,
            "length": 450,
            "slope": -15000
          },
          {
            "start": 1375,
            "length": 175,
            "slope": 10000
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
        "finishTimeMin": 110,
        "finishTimeMax": 118,
        "corners": [
          {
            "start": 300,
            "length": 200
          },
          {
            "start": 500,
            "length": 200
          },
          {
            "start": 1100,
            "length": 200
          },
          {
            "start": 1300,
            "length": 200
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 300
          },
          {
            "start": 700,
            "end": 1100
          },
          {
            "start": 1500,
            "end": 1800
          }
        ],
        "slopes": [
          {
            "start": 0,
            "length": 150,
            "slope": 10000
          },
          {
            "start": 700,
            "length": 450,
            "slope": -15000
          },
          {
            "start": 1575,
            "length": 175,
            "slope": 10000
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
        "finishTimeMin": 121,
        "finishTimeMax": 129,
        "corners": [
          {
            "start": 500,
            "length": 200
          },
          {
            "start": 700,
            "length": 200
          },
          {
            "start": 1300,
            "length": 200
          },
          {
            "start": 1500,
            "length": 200
          }
        ],
        "straights": [
          {
            "start": 100,
            "end": 500
          },
          {
            "start": 900,
            "end": 1300
          },
          {
            "start": 1700,
            "end": 2000
          }
        ],
        "slopes": [
          {
            "start": 175,
            "length": 175,
            "slope": 10000
          },
          {
            "start": 900,
            "length": 375,
            "slope": 10000
          },
          {
            "start": 1275,
            "length": 450,
            "slope": -15000
          },
          {
            "start": 1775,
            "length": 175,
            "slope": 10000
          }
        ]
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
        "finishTimeMax": 57,
        "corners": [
          {
            "start": 200,
            "length": 200
          },
          {
            "start": 400,
            "length": 200
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
        "finishTimeMin": 141.6,
        "finishTimeMax": 149,
        "corners": [
          {
            "start": 1000,
            "length": 417
          },
          {
            "start": 1417,
            "length": 200
          }
        ],
        "straights": [
          {
            "start": 0,
            "end": 1000
          },
          {
            "start": 1617,
            "end": 1866
          },
          {
            "start": 1867,
            "end": 2400
          }
        ],
        "slopes": [
          {
            "start": 400,
            "length": 600,
            "slope": 20000
          },
          {
            "start": 1017,
            "length": 383,
            "slope": -20000
          },
          {
            "start": 1400,
            "length": 217,
            "slope": -15000
          }
        ]
      }
    }
  }
}
""".trimIndent()

internal val rawNormalSkillData = """
 [
    {
      "variants": [{ "rarity": "rare", "id": 202051, "name": "大逃げ" }],
      "type": "passive",
      "conditions": { "running_style": 1 },
    },
    {
      "variants": [
        { "rarity": "double", "id": 200021, "name": "左回り◎", "passiveSpeed": 60 },
        {
          "rarity": "normal",
          "id": 200022,
          "name": "左回り○",
          "passiveSpeed": 40,
        },
      ],
      "conditions": { "rotation": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106701211,
          "holder": 106701,
          "name": "右回りの輪舞曲",
          "passiveSpeed": 80,
          "passivePower": 80,
        },
        {
          "rarity": "rare",
          "id": 200014,
          "name": "右回りの鬼",
          "passiveSpeed": 60,
          "passivePower": 60,
        },
        { "rarity": "double", "id": 200011, "name": "右回り◎", "passiveSpeed": 60 },
        {
          "rarity": "normal",
          "id": 200012,
          "name": "右回り○",
          "passiveSpeed": 40,
        },
      ],
      "conditions": { "rotation": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106102211,
          "holder": 106102,
          "name": "春風吹きて、桜舞う",
          "passiveSpeed": 80,
          "passivePower": 80,
        },
        {
          "rarity": "evo",
          "id": 106901211,
          "holder": 106901,
          "name": "けふ九重に満開です",
          "passiveSpeed": 80,
          "passivePower": 80,
        },
        {
          "rarity": "evo",
          "id": 101702211,
          "holder": 101702,
          "name": "風霜高潔",
          "passiveSpeed": 80,
          "passivePower": 80,
        },
        {
          "rarity": "rare",
          "id": -200174,
          "name": "春一番 | 青嵐 | 初嵐",
          "passiveSpeed": 60,
          "passivePower": 60,
        },
        {
          "rarity": "double",
          "id": -200171,
          "name": "季節ウマ娘◎",
          "passiveSpeed": 60,
        },
        {
          "rarity": "normal",
          "id": -200172,
          "name": "季節ウマ娘○",
          "passiveSpeed": 40,
        },
      ],
      "emulatorTypeLimit": ["cm"],
    },
    {
      "variants": [
        { "rarity": "double", "id": 200271, "name": "おひとり様◎", "passiveSpeed": 80 },
        {
          "rarity": "normal",
          "id": 200272,
          "name": "おひとり様○",
          "passiveSpeed": 60,
        },
      ],
    },
    {
      "variants": [
        { "rarity": "double", "id": 200301, "name": "伏兵◎", "passiveSpeed": 60 },
        {
          "rarity": "normal",
          "id": 200302,
          "name": "伏兵○",
          "passiveSpeed": 40,
        },
      ],
    },
    {
      "variants": [
        { "rarity": "double", "id": 200261, "name": "外枠得意◎", "passiveSpeed": 60 },
        {
          "rarity": "normal",
          "id": 200262,
          "name": "外枠得意○",
          "passiveSpeed": 40,
        },
      ],
      "tooltip": "発動率44%として扱う(チャンピオンズミーティング基準)",
      "triggerRate": 0.4444444444444444,
    },
    {
      "variants": [
        {
          "rarity": "normal",
          "id": 201631,
          "name": "シンパシー",
          "passiveSpeed": 40,
        },
      ],
    },
    {
      "variants": [
        {
          "rarity": "normal",
          "id": 201641,
          "name": "一匹狼",
          "passiveSpeed": 40,
        },
      ],
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 200081,
          "name": "札幌レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 200082,
          "name": "札幌レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10001 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 200091,
          "name": "函館レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 200092,
          "name": "函館レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10002 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 200111,
          "name": "新潟レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 200112,
          "name": "新潟レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10003 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 200101,
          "name": "福島レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 200102,
          "name": "福島レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10004 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 200041,
          "name": "中山レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 200042,
          "name": "中山レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10005 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 200031,
          "name": "東京レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 200032,
          "name": "東京レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10006 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 200071,
          "name": "中京レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 200072,
          "name": "中京レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10007 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105901211,
          "holder": 105901,
          "name": "淀の女王",
          "passiveSpeed": 80,
          "passiveStamina": 80,
          "passiveWisdom": 80,
        },
        {
          "rarity": "rare",
          "id": 200064,
          "name": "淀の申し子",
          "passiveSpeed": 60,
          "passiveStamina": 60,
          "passiveWisdom": 60,
        },
        {
          "rarity": "double",
          "id": 200061,
          "name": "京都レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 200062,
          "name": "京都レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10008 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 200051,
          "name": "阪神レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 200052,
          "name": "阪神レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10009 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 200121,
          "name": "小倉レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 200122,
          "name": "小倉レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10010 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 200951,
          "name": "大井レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 200952,
          "name": "大井レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10011 },
    },
    {
      "variants": [
        { "rarity": "double", "id": 200131, "name": "根幹距離◎", "passiveStamina": 60 },
        {
          "rarity": "normal",
          "id": 200132,
          "name": "根幹距離○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "is_basis_distance": 1 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 200141,
          "name": "非根幹距離◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 200142,
          "name": "非根幹距離○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "is_basis_distance": 0 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 200154,
          "holder": 107701,
          "name": "ターフの主人公",
          "invokes": [
            {
              "passivePower": 80,
              "passiveSpeed": 80,
              "conditions": { "ground_condition": 1 },
            },
            {
              "passivePower": 40,
              "passiveSpeed": 40,
              "conditions": { "ground_condition": [2, 3, 4] },
            },
          ],
        },
        {
          "rarity": "rare",
          "id": 200154,
          "name": "良バ場の鬼",
          "passivePower": 60,
          "passiveSpeed": 60,
        },
        {
          "rarity": "double",
          "id": 200151,
          "name": "良バ場◎",
          "passivePower": 60,
        },
        {
          "rarity": "normal",
          "id": 200152,
          "name": "良バ場○",
          "passivePower": 40,
        },
      ],
      "conditions": { "ground_condition": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100703211,
          "holder": 100703,
          "name": "荒磯好みの黄金船",
          "passivePower": 80,
          "passiveSpeed": 80,
          "trigger": "function () {
            thiz.startDelay += 0.1;
          }",
        },
        {
          "rarity": "rare",
          "id": 200164,
          "name": "道悪の鬼",
          "passivePower": 60,
          "passiveSpeed": 60,
        },
        { "rarity": "double", "id": 200161, "name": "道悪◎", "passivePower": 60 },
        {
          "rarity": "normal",
          "id": 200162,
          "name": "道悪○",
          "passivePower": 40,
        },
      ],
      "conditions": { "ground_condition": [2, 3, 4] },
    },
    {
      "variants": [
        { "rarity": "double", "id": 200281, "name": "対抗意識◎", "passivePower": 60 },
        {
          "rarity": "normal",
          "id": 200282,
          "name": "対抗意識○",
          "passivePower": 40,
        },
      ],
    },
    {
      "variants": [
        { "rarity": "double", "id": -200211, "name": "天気の日◎", "passiveGuts": 60 },
        {
          "rarity": "normal",
          "id": -200212,
          "name": "天気の日○",
          "passiveGuts": 40,
        },
      ],
      "emulatorTypeLimit": ["cm"],
    },
    {
      "variants": [
        { "rarity": "double", "id": 200291, "name": "徹底マーク◎", "passiveGuts": 60 },
        {
          "rarity": "normal",
          "id": 200292,
          "name": "徹底マーク○",
          "passiveGuts": 40,
        },
      ],
    },
    {
      "variants": [
        { "rarity": "double", "id": 200251, "name": "内枠得意◎", "passiveWisdom": 60 },
        {
          "rarity": "normal",
          "id": 200252,
          "name": "内枠得意○",
          "passiveWisdom": 40,
        },
      ],
      "tooltip": "発動率33%として扱う(チャンピオンズミーティング基準)",
      "triggerRate": 0.3333333333333333,
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 201521,
          "name": "逃げのコツ◎",
          "passiveWisdom": 60,
        },
        {
          "rarity": "normal",
          "id": 201522,
          "name": "逃げのコツ○",
          "passiveWisdom": 40,
        },
      ],
      "conditions": { "running_style": 1 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 201531,
          "name": "先行のコツ◎",
          "passiveWisdom": 60,
        },
        {
          "rarity": "normal",
          "id": 201532,
          "name": "先行のコツ○",
          "passiveWisdom": 40,
        },
      ],
      "conditions": { "running_style": 2 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 201541,
          "name": "差しのコツ◎",
          "passiveWisdom": 60,
        },
        {
          "rarity": "normal",
          "id": 201542,
          "name": "差しのコツ○",
          "passiveWisdom": 40,
        },
      ],
      "conditions": { "running_style": 3 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 201551,
          "name": "追込のコツ◎",
          "passiveWisdom": 60,
        },
        {
          "rarity": "normal",
          "id": 201552,
          "name": "追込のコツ○",
          "passiveWisdom": 40,
        },
      ],
      "conditions": { "running_style": 4 },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 201561,
          "name": "スーパーラッキーセブン",
          "passiveSpeed": 60,
          "passivePower": 60,
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 201562,
          "name": "ラッキーセブン",
          "passiveSpeed": 40,
          "passivePower": 40,
          "passiveStamina": 40,
        },
      ],
      "tooltip": "発動率1/18として扱う(チャンピオンズミーティング基準)",
      "triggerRate": 0.05555555555555555,
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105601211,
          "holder": 105601,
          "name": "七福即生",
          "passiveSpeed": 80,
          "passivePower": 80,
          "passiveStamina": 80,
        },
      ],
      "tooltip": "発動率2/9として扱う(チャンピオンズミーティング基準)",
      "triggerRate": 0.2222222,
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106102211,
          "holder": 106102,
          "name": "春風吹きて、桜舞う",
          "passiveSpeed": 80,
          "passivePower": 80,
        },
        {
          "rarity": "evo",
          "id": 106901211,
          "holder": 106901,
          "name": "けふ九重に満開です",
          "passiveSpeed": 80,
          "passivePower": 80,
        },
        {
          "rarity": "rare",
          "id": 200174,
          "name": "春一番",
          "passiveSpeed": 60,
          "passivePower": 60,
        },
        { "rarity": "double", "id": 200171, "name": "春ウマ娘◎", "passiveSpeed": 60 },
        {
          "rarity": "normal",
          "id": 200172,
          "name": "春ウマ娘○",
          "passiveSpeed": 40,
        },
      ],
      "tooltip": "発動率40%として扱う。",
      "emulatorTypeLimit": ["team"],
      "check": "function () {
        return thiz.season === 0;
      }",
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 200184,
          "name": "青嵐",
          "passiveSpeed": 60,
          "passivePower": 60,
        },
        { "rarity": "double", "id": 200181, "name": "夏ウマ娘◎", "passiveSpeed": 60 },
        {
          "rarity": "normal",
          "id": 200182,
          "name": "夏ウマ娘○",
          "passiveSpeed": 40,
        },
      ],
      "tooltip": "発動率20%として扱う。",
      "emulatorTypeLimit": ["team"],
      "check": "function () {
        return thiz.season === 1;
      }",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101702211,
          "holder": 101702,
          "name": "風霜高潔",
          "passiveSpeed": 80,
          "passivePower": 80,
        },
        {
          "rarity": "rare",
          "id": 200194,
          "name": "初嵐",
          "passiveSpeed": 60,
          "passivePower": 60,
        },
        { "rarity": "double", "id": 200191, "name": "秋ウマ娘◎", "passiveSpeed": 60 },
        {
          "rarity": "normal",
          "id": 200192,
          "name": "秋ウマ娘○",
          "passiveSpeed": 40,
        },
      ],
      "tooltip": "発動率20%として扱う。",
      "emulatorTypeLimit": ["team"],
      "check": "function () {
        return thiz.season === 2;
      }",
    },
    {
      "variants": [
        { "rarity": "double", "id": 200201, "name": "冬ウマ娘◎", "passiveSpeed": 60 },
        {
          "rarity": "normal",
          "id": 200202,
          "name": "冬ウマ娘○",
          "passiveSpeed": 40,
        },
      ],
      "tooltip": "発動率20%として扱う。",
      "emulatorTypeLimit": ["team"],
      "check": "function () {
        return thiz.season === 3;
      }",
    },
    {
      "variants": [
        { "rarity": "double", "id": 200211, "name": "晴れの日◎", "passiveGuts": 60 },
        {
          "rarity": "normal",
          "id": 200212,
          "name": "晴れの日○",
          "passiveGuts": 40,
        },
      ],
      "tooltip": "発動率57.5%として扱う。",
      "emulatorTypeLimit": ["team"],
      "check": "function () {
        return thiz.weather === 0;
      }",
    },
    {
      "variants": [
        { "rarity": "double", "id": 200221, "name": "曇りの日◎", "passiveGuts": 60 },
        {
          "rarity": "normal",
          "id": 200222,
          "name": "曇りの日○",
          "passiveGuts": 40,
        },
      ],
      "tooltip": "発動率30%として扱う。",
      "emulatorTypeLimit": ["team"],
      "check": "function () {
        return thiz.weather === 1;
      }",
    },
    {
      "variants": [
        { "rarity": "double", "id": 200231, "name": "雨の日◎", "passiveGuts": 60 },
        {
          "rarity": "normal",
          "id": 200232,
          "name": "雨の日○",
          "passiveGuts": 40,
        },
      ],
      "tooltip": "発動率11%として扱う。",
      "emulatorTypeLimit": ["team"],
      "check": "function () {
        return thiz.weather === 2;
      }",
    },
    {
      "variants": [
        { "rarity": "double", "id": 200241, "name": "雪の日◎", "passiveGuts": 60 },
        {
          "rarity": "normal",
          "id": 200242,
          "name": "雪の日○",
          "passiveGuts": 40,
        },
      ],
      "tooltip": "発動率1.5%として扱う。",
      "emulatorTypeLimit": ["team"],
      "check": "function () {
        return thiz.weather === 3;
      }",
    },
    {
      "variants": [
        {
          "rarity": "normal",
          "id": 202161,
          "name": "自制心",
          "passiveWisdom": 60,
          "temptationRate": -3,
        },
      ],
      "tooltip": "掛かり率は固定で-3%として処理",
    },
    {
      "variants": [
        { "rarity": "double", "id": 202251, "name": "交流重賞◎", "passiveSpeed": 60 },
        {
          "rarity": "normal",
          "id": 202252,
          "name": "交流重賞○",
          "passiveSpeed": 40,
        },
      ],
      "conditions": { "ground_type": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 109801211,
          "holder": 109801,
          "name": "龍脈の波濤",
        },
      ],
      "type": "passive",
      "conditions": { "ground_type": 2 },
      "trigger": "function () {
        if (thiz.umaStatus.power >= 1200) {
          thiz.passiveBonus.speed += 100;
        } else if (thiz.umaStatus.power >= 1000) {
          thiz.passiveBonus.speed += 80;
        }
      }",
    },
    {
      "variants": [{ "rarity": "rare", "id": 202331, "name": "抜群の踏み込み" }],
      "type": "passive",
      "conditions": { "ground_type": 2 },
      "trigger": "function () {
        if (thiz.umaStatus.power >= 1200) {
          thiz.passiveBonus.speed += 80;
        } else if (thiz.umaStatus.power >= 1000) {
          thiz.passiveBonus.speed += 60;
        }
      }",
    },
    {
      "variants": [{ "rarity": "normal", "id": 202332, "name": "踏み込み上手" }],
      "type": "passive",
      "conditions": { "ground_type": 2 },
      "trigger": "function () {
        if (thiz.umaStatus.power >= 1200) {
          thiz.passiveBonus.speed += 40;
        } else if (thiz.umaStatus.power >= 1000) {
          thiz.passiveBonus.speed += 20;
        }
      }",
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 202201,
          "name": "川崎レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 202202,
          "name": "川崎レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10103 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 202211,
          "name": "船橋レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 202212,
          "name": "船橋レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10104 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 202221,
          "name": "盛岡レース場◎",
          "passiveStamina": 60,
        },
        {
          "rarity": "normal",
          "id": 202222,
          "name": "盛岡レース場○",
          "passiveStamina": 40,
        },
      ],
      "conditions": { "track_id": 10105 },
    },
    {
      "variants": [
        { "rarity": "double", "id": 202231, "name": "ナイター◎", "passiveWisdom": 60 },
        {
          "rarity": "normal",
          "id": 202232,
          "name": "ナイター○",
          "passiveWisdom": 40,
        },
      ],
    },
    {
      "variants": [
        { "rarity": "double", "id": 202241, "name": "小回り◎", "passiveWisdom": 60 },
        {
          "rarity": "normal",
          "id": 202242,
          "name": "小回り○",
          "passiveWisdom": 40,
        },
      ],
      "conditions": { "track_id": [10001, 10002, 10004, 10010, 10103, 10104] },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104301211,
          "holder": 104301,
          "name": "いたずらマイスター",
          "passiveSpeed": 80,
          "passivePower": 80,
        },
        {
          "rarity": "rare",
          "id": 202341,
          "name": "泥んこマイスター",
          "passiveSpeed": 60,
          "passivePower": 60,
        },
        { "rarity": "double", "id": 202342, "name": "泥遊び◎", "passiveSpeed": 60 },
        {
          "rarity": "normal",
          "id": 202343,
          "name": "泥遊び○",
          "passiveSpeed": 40,
        },
      ],
      "conditions": { "ground_type": 2, "ground_condition": [3, 4] },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104901211,
          "holder": 104901,
          "name": "鉄火のギャンブラー",
          "passiveSpeed": 100,
          "passiveGuts": 100,
          "passivePower": 100,
        },
        {
          "rarity": "rare",
          "id": 202441,
          "name": "勝負師",
          "passiveSpeed": 80,
          "passiveGuts": 80,
          "passivePower": 80,
        },
        {
          "rarity": "normal",
          "id": 202442,
          "name": "やまっけ",
          "passiveSpeed": 40,
          "passiveGuts": 40,
          "passivePower": 40,
        },
      ],
      "tooltip": "発動率60%として扱う",
      "triggerRate": 0.6,
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105802211,
          "holder": 105802,
          "name": "魂の導き手",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 104501211,
          "holder": 104501,
          "name": "癒しのマエストロ",
          "heal": 750,
        },
        {
          "rarity": "evo",
          "id": 102402211,
          "holder": 102402,
          "name": "バレルロール",
          "heal": 750,
        },
        {
          "rarity": "evo",
          "id": 101502111,
          "holder": 101502,
          "name": "円舞曲のマエストロ",
          "heal": 750,
        },
        {
          "rarity": "rare",
          "id": 200351,
          "name": "円弧のマエストロ",
          "heal": 550,
        },
        {
          "rarity": "normal",
          "id": 200352,
          "name": "コーナー回復○",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "corner_random": [1, 2, 3, 4] },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102401111,
          "holder": 102401,
          "name": "You copy?",
          "heal": 750,
        },
        {
          "rarity": "evo",
          "id": 100801111,
          "holder": 100801,
          "name": "スキットルブレイク",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "rare",
          "id": 200381,
          "name": "好転一息",
          "heal": 550,
        },
        {
          "rarity": "normal",
          "id": 200382,
          "name": "直線回復",
          "heal": 150,
        },
      ],
      "conditions": { "corner": 0, "phase": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103001211,
          "holder": 103001,
          "name": "黒の刺客",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 102301211,
          "holder": 102301,
          "name": "計画的クールダウン",
          "heal": 750,
        },
        {
          "rarity": "evo",
          "id": 101302111,
          "holder": 101302,
          "name": "アクティブレスト",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 101102111,
          "holder": 101102,
          "name": "キュアリーヒール",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "rare",
          "id": 200741,
          "name": "クールダウン",
          "heal": 550,
        },
        {
          "rarity": "normal",
          "id": 200742,
          "name": "深呼吸",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "distance_type": 4, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "normal",
          "id": 201571,
          "name": "スリーセブン",
          "heal": 150,
        },
      ],
      "conditions": { "remain_distance": [776, 778] },
    },
    {
      "variants": [{ "rarity": "normal", "id": 201621, "name": "ふり絞り", "heal": 150 }],
      "check": "function () {
        return thiz.skillTriggerCount[2] + thiz.skillTriggerCount[3] >= 3;
      }",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104401111,
          "holder": 104401,
          "name": "放課後魔法少女",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 100701211,
          "holder": 100701,
          "name": "エクストリーム下校術",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "rare",
          "id": 201481,
          "name": "下校後のスペシャリスト",
          "heal": 550,
        },
        { "rarity": "normal", "id": 201482, "name": "下校の楽しみ", "heal": 150 },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "running_style": 4, "slope": 2, "accumulatetime": 10 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103101111,
          "holder": 103101,
          "name": "じゃじゃウマお姉ちゃん",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 100902111,
          "holder": 100902,
          "name": "おてんば女王",
          "heal": 750,
        },
        {
          "rarity": "evo",
          "id": 100402111,
          "holder": 100402,
          "name": "ドキッ☆じゃじゃウマ娘！",
          "heal": 750,
        },
        { "rarity": "rare", "id": 201281, "name": "じゃじゃウマ娘", "heal": 550 },
        {
          "rarity": "normal",
          "id": 201282,
          "name": "勢い任せ",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "running_style": 1, "slope": 1, "accumulatetime": 10 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102801211,
          "holder": 102801,
          "name": "ボーノな健啖家",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 100601111,
          "holder": 100601,
          "name": "笠松の食いしん坊",
          "heal": 750,
        },
        {
          "rarity": "evo",
          "id": 100601121,
          "holder": 100601,
          "name": "飢えた怪物",
          "heal": 150,
          "targetSpeed": 0.35,
        },
        {
          "rarity": "evo",
          "id": 100602111,
          "holder": 100602,
          "name": "ドカ食い養生",
          "heal": 750,
        },
        {
          "rarity": "evo",
          "id": 100101111,
          "holder": 100101,
          "name": "はらぺこ大将",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "rare",
          "id": 201351,
          "name": "食いしん坊",
          "heal": 550,
        },
        {
          "rarity": "normal",
          "id": 201352,
          "name": "栄養補給",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "running_style": 2, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 107101111,
          "holder": 107101,
          "name": "永遠の輝き",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 104801211,
          "holder": 104801,
          "name": "トレンドプランナー",
          "heal": 550,
          "targetSpeed": 0.15,
          "conditions": { "running_style": [2, 3], "phase_random": 1 },
        },
        {
          "rarity": "evo",
          "id": 104701111,
          "holder": 104701,
          "name": "英雄への道標",
          "heal": 750,
        },
        {
          "rarity": "evo",
          "id": 104701121,
          "holder": 104701,
          "name": "物語を紡ぐ者",
          "heal": 150,
          "targetSpeed": 0.35,
        },
        {
          "rarity": "evo",
          "id": 103201211,
          "holder": 103201,
          "name": "効率的休息法",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 100901211,
          "holder": 100901,
          "name": "アタシが勝つんだから！",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 100501211,
          "holder": 100501,
          "name": "エンターテイナー",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 100302111,
          "holder": 100302,
          "name": "レースの天才",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "rare",
          "id": 200571,
          "name": "レースプランナー",
          "heal": 550,
        },
        {
          "rarity": "normal",
          "id": 200572,
          "name": "好位追走",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "running_style": 2, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "normal",
          "id": 200662,
          "name": "様子見",
          "heal": 150,
          "acceleration": 0.1,
        },
      ],
      "conditions": {
        "distance_type": 2,
        "phase_random": 1,
        "running_style": [3, 4],
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103602111,
          "holder": 103602,
          "name": "Gluttony’s Grip",
          "heal": 550,
        },
        { "rarity": "rare", "id": 201221, "name": "スタミナグリード", "heal": 350 },
        {
          "rarity": "normal",
          "id": 201222,
          "name": "スタミナイーター",
          "heal": 150,
        },
      ],
      "conditions": {
        "distance_type": 4,
        "phase_random": 1,
        "running_style": [3, 4],
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102601211,
          "holder": 102601,
          "name": "困難を乗り越える者",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        { "rarity": "rare", "id": 200711, "name": "切り開く者", "heal": 550 },
        {
          "rarity": "normal",
          "id": 200712,
          "name": "前途洋々",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": {
        "distance_type": 3,
        "phase_random": 1,
        "running_style": [1, 2],
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105001111,
          "holder": 105001,
          "name": "鬼気森然",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        { "rarity": "rare", "id": 200621, "name": "眠れる獅子", "heal": 550 },
        {
          "rarity": "normal",
          "id": 200622,
          "name": "後方待機",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": {
        "running_style": 4,
        "phase_random": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102302111,
          "holder": 102302,
          "name": "VIPな後ろ姿",
          "heal": 750,
        },
        { "rarity": "rare", "id": 201201, "name": "VIP顔パス", "heal": 550 },
        {
          "rarity": "normal",
          "id": 201202,
          "name": "パス上手",
          "heal": 150,
        },
      ],
      "conditions": {
        "distance_type": 4,
        "accumulatetime": 5,
        "phase_random": 1,
      },
      "tooltip": "「中盤のどこかで発動」として扱う。適当実装注意。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100302211,
          "holder": 100302,
          "name": "地平の彼方まで",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        { "rarity": "rare", "id": 201141, "name": "神業ステップ", "heal": 550 },
        {
          "rarity": "normal",
          "id": 201142,
          "name": "軽やかステップ",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "distance_type": 3, "accumulatetime": 20 },
      "tooltip":
        "「スタート後20秒で発動」として扱う。大体そこら辺で内コースを取り始めるため。多分（ガバ）",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 109901111,
          "holder": 109901,
          "name": "苫小牧グルメで舌鼓！",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 103002111,
          "holder": 103002,
          "name": "がんばるぞー…おー！",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 102301111,
          "holder": 102301,
          "name": "想定通り、余裕十分",
          "heal": 750,
        },
        {
          "rarity": "evo",
          "id": 101702111,
          "holder": 101702,
          "name": "神色自若",
          "heal": 750,
        },
        {
          "rarity": "evo",
          "id": 101401211,
          "holder": 101401,
          "name": "余裕のパフォーマンス",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 101301211,
          "holder": 101301,
          "name": "名優の余裕",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        { "rarity": "rare", "id": 200561, "name": "余裕綽々", "heal": 550 },
        {
          "rarity": "normal",
          "id": 200562,
          "name": "スタミナキープ",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "running_style": 2, "phase_laterhalf_random": 0 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104001111,
          "holder": 104001,
          "name": "先見の明",
          "heal": 550,
          "targetSpeed": 0.25,
        },
        { "rarity": "rare", "id": 200691, "name": "慧眼", "heal": 550 },
        {
          "rarity": "normal",
          "id": 200692,
          "name": "展開窺い",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": {
        "distance_type": 2,
        "phase_laterhalf_random": 0,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102501111,
          "holder": 102501,
          "name": "コーヒーブレイク",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 101102211,
          "holder": 101102,
          "name": "勇気の魔法",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 110601111,
          "holder": 110601,
          "name": "焦らず、気負わず",
          "heal": 750,
        },
        {
          "rarity": "rare",
          "id": 201421,
          "name": "リラックス",
          "heal": 550,
        },
        {
          "rarity": "normal",
          "id": 201422,
          "name": "小休憩",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "running_style": 3, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105201111,
          "holder": 105201,
          "name": "まだまだだよ！",
          "heal": 750,
        },
        { "rarity": "rare", "id": 200441, "name": "鋼の意志", "heal": 550 },
        {
          "rarity": "normal",
          "id": 200442,
          "name": "隠れ蓑",
          "heal": 150,
        },
      ],
      "conditions": { "phase_random": 1, "accumulatetime": 5 },
      "tooltip": "「中盤のどこか」として扱う。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103301111,
          "holder": 103301,
          "name": "静かな誓い",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        { "rarity": "rare", "id": 201491, "name": "冷静沈着", "heal": 550 },
        {
          "rarity": "normal",
          "id": 201492,
          "name": "冷静",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "tooltip": "「中盤のどこかで発動」として扱う。",
      "conditions": { "running_style": 4, "accumulatetime": 10, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 107401111,
          "holder": 107401,
          "name": "不屈のお嬢様",
          "heal": 750,
        },
        {
          "rarity": "evo",
          "id": 105801211,
          "holder": 105801,
          "name": "諦めない、ですぅ～！",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 105201211,
          "holder": 105201,
          "name": "まけないからね！",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 107102111,
          "holder": 107102,
          "name": "美しき燐光",
          "heal": 550,
          "targetSpeed": 0.15,
          "conditions": {
            "running_style": 2,
            "phase_laterhalf_random": 1,
          },
        },
        {
          "rarity": "rare",
          "id": 200471,
          "name": "不屈の心",
          "heal": 550,
        },
        {
          "rarity": "normal",
          "id": 200472,
          "name": "ペースキープ",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "phase_random": 1 },
      "tooltip": "「中盤のどこか」として扱う。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106201111,
          "holder": 106201,
          "name": "ゴーイングマイウェイ",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "rare",
          "id": 200481,
          "name": "どこ吹く風",
          "heal": 550,
        },
        {
          "rarity": "normal",
          "id": 200482,
          "name": "ウマ込み冷静",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "phase_random": 1 },
      "tooltip": "「中盤のどこか」として扱う。",
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 200761,
          "name": "火事場のバ鹿力",
          "heal": 550,
          "targetSpeed": 0.35,
        },
        { "rarity": "normal", "id": 200762, "name": "別腹タンク", "heal": 150 },
      ],
      "duration": 1.8,
      "conditions": { "distance_type": 4, "hp_per": "<=30" },
    },
    {
      "variants": [
        { "rarity": "rare", "id": 210021, "name": "アオハル燃焼・体", "heal": 605 },
        {
          "rarity": "normal",
          "id": 210022,
          "name": "アオハル点火・体",
          "heal": 165,
        },
      ],
      "conditions": { "phase_random": 1 },
      "tooltip": "効果1.1倍として扱う",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106701111,
          "holder": 106701,
          "name": "金剛不壊",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 106701121,
          "holder": 106701,
          "name": "隠せぬ輝き",
          "heal": 150,
          "targetSpeed": 0.35,
        },
        {
          "rarity": "evo",
          "id": 104002211,
          "holder": 104002,
          "name": "虎視眈々",
          "heal": 550,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 106202111,
          "holder": 106202,
          "name": "ポジティブスマイル",
          "heal": 750,
        },
        {
          "rarity": "rare",
          "id": 201691,
          "name": "潜伏態勢",
          "heal": 550,
        },
        {
          "rarity": "normal",
          "id": 201692,
          "name": "静かな呼吸",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "running_style": 3, "phase_laterhalf_random": 0 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103401211,
          "holder": 103401,
          "name": "熟練の砂塵使い",
          "heal": 750,
          "targetSpeed": 0.35,
        },
        {
          "rarity": "evo",
          "id": 103401221,
          "holder": 103401,
          "name": "粋でいなせな達人技",
          "heal": 350,
          "targetSpeed": 0.45,
          "conditions": {
            "distance_type": 3,
            "phase_random": 1,
          },
        },
        { "rarity": "rare", "id": 202001, "name": "砂の玄人", "heal": 550 },
        {
          "rarity": "normal",
          "id": 202002,
          "name": "砂塵慣れ",
          "heal": 150,
        },
      ],
      "type": "heal",
      "duration": 1.8,
      "conditions": { "ground_type": 2, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 107401211,
          "holder": 107401,
          "name": "動かざること羊蹄山の若し",
          "heal": 950,
          "speed": -0.15,
        },
        {
          "rarity": "rare",
          "id": 202071,
          "name": "泰然自若",
          "heal": 750,
          "speed": -0.15,
        },
        {
          "rarity": "normal",
          "id": 202072,
          "name": "マイペース",
          "heal": 350,
          "speed": -0.15,
        },
      ],
      "type": "heal",
      "duration": 1.2,
      "conditions": { "distance_type": 4, "phase_firsthalf_random": 1 },
      "tooltip": "4-7位",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101303211,
          "holder": 101303,
          "name": "一意専心にリフレッシュ！",
          "heal": 950,
        },
        { "rarity": "rare", "id": 202191, "name": "一意専心", "heal": 750 },
        {
          "rarity": "normal",
          "id": 202192,
          "name": "抜かりなし",
          "heal": 350,
        },
      ],
      "conditions": {
        "is_badstart": 0,
        "running_style": 2,
        "distance_type": 4,
        "phase_firsthalf_random": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104502111,
          "holder": 104502,
          "name": "お世話のプロフェッショナル",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 101701211,
          "holder": 101701,
          "name": "鎧袖一触",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "rare",
          "id": 200331,
          "name": "弧線のプロフェッサー",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 200332,
          "name": "コーナー巧者○",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 2.4,
      "cd": 30,
      "conditions": { "all_corner_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 200971,
          "name": "短距離コーナー◎",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 200972,
          "name": "短距離コーナー○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "all_corner_random": 1, "distance_type": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105102111,
          "holder": 105102,
          "name": "祝福のフラワーガール",
          "targetSpeed": 0.35,
          "tooltip": "バフ効果は無視",
        },
        {
          "rarity": "rare",
          "id": 201043,
          "name": "豪風円刃",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "double",
          "id": 201041,
          "name": "マイルコーナー◎",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 201042,
          "name": "マイルコーナー○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "all_corner_random": 1, "distance_type": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104701211,
          "holder": 104701,
          "name": "光輝く剣",
          "targetSpeed": 0.45,
        },
      ],
      "duration": 3,
      "conditions": { "all_corner_random": 1, "distance_type": [3, 4] },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106703211,
          "holder": 106703,
          "name": "ダンス・デ・レぺ",
          "invokes": [
            {
              "targetSpeed": 0.45,
              "duration": 4,
              "conditions": {
                "all_corner_random": 1,
                "distance_type": 3,
                "track_id": 10201,
                "motivation": 5,
              },
            },
            {
              "targetSpeed": 0.45,
            },
          ],
          "type": "speed",
        },
        {
          "rarity": "rare",
          "id": 201113,
          "name": "光芒円刃",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "double",
          "id": 201111,
          "name": "中距離コーナー◎",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 201112,
          "name": "中距離コーナー○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "all_corner_random": 1, "distance_type": 3 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 201181,
          "name": "長距離コーナー◎",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 201182,
          "name": "長距離コーナー○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "all_corner_random": 1, "distance_type": 4 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103102211,
          "holder": 103102,
          "name": "華麗なサーブ",
          "targetSpeed": 0.35,
          "acceleration": 0.2,
        },
        {
          "rarity": "rare",
          "id": 201253,
          "name": "陣風円刃",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "double",
          "id": 201251,
          "name": "逃げコーナー◎",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 201252,
          "name": "逃げコーナー○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "all_corner_random": 1, "running_style": 1 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 201321,
          "name": "先行コーナー◎",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 201322,
          "name": "先行コーナー○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "all_corner_random": 1, "running_style": 2 },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 201393,
          "name": "鋭脚円刃",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "double",
          "id": 201391,
          "name": "差しコーナー◎",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 201392,
          "name": "差しコーナー○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "all_corner_random": 1, "running_style": 3 },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 201463,
          "name": "月影円刃",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "double",
          "id": 201461,
          "name": "追込コーナー◎",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 201462,
          "name": "追込コーナー○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "all_corner_random": 1, "running_style": 4 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101601111,
          "holder": 101601,
          "name": "一刀両断",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 101302211,
          "holder": 101302,
          "name": "空の果てまで",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 105101111,
          "holder": 105101,
          "name": "風花疾走",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 106902111,
          "holder": 106902,
          "name": "桜花爛漫一直線",
          "targetSpeed": 0.45,
        },
        { "rarity": "rare", "id": 200361, "name": "ハヤテ一文字", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 200362,
          "name": "直線巧者",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 3,
      "cd": 30,
      "conditions": { "straight_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104201111,
          "holder": 104201,
          "name": "世界を貫く至高の輝き",
          "targetSpeed": 0.35,
          "speedWithDecel": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": [1, 2], "straight_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104102211,
          "holder": 104102,
          "name": "驀進！爆進！バクシーン！",
          "duration": 4,
        },
        {
          "rarity": "rare",
          "id": 200963,
          "name": "紫電一閃",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "double",
          "id": 200961,
          "name": "短距離直線◎",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 200962,
          "name": "短距離直線○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": 1, "straight_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "double",
          "id": 201031,
          "name": "マイル直線◎",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 201032,
          "name": "マイル直線○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": 2, "straight_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103702211,
          "holder": 103702,
          "name": "黒い閃光",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 107102211,
          "holder": 107102,
          "name": "淡く儚い残照",
          "targetSpeed": 0.55,
          "heal": -200,
          "conditions": {
            "distance_type": 3,
            "running_style": 1,
            "is_lastspurt": 1,
            "corner": 0,
          },
        },
        {
          "rarity": "rare",
          "id": 201103,
          "name": "光芒一閃",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "double",
          "id": 201101,
          "name": "中距離直線◎",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 201102,
          "name": "中距離直線○",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 3,
      "conditions": { "distance_type": 3, "straight_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106801211,
          "holder": 106801,
          "name": "爆風一閃！",
          "targetSpeed": 0.45,
        },
        { "rarity": "rare", "id": 201173, "name": "烈風一閃", "targetSpeed": 0.35 },
        {
          "rarity": "double",
          "id": 201171,
          "name": "長距離直線◎",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 201172,
          "name": "長距離直線○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": 4, "straight_random": 1 },
    },
    {
      "variants": [
        { "rarity": "double", "id": 201241, "name": "逃げ直線◎", "targetSpeed": 0.25 },
        {
          "rarity": "normal",
          "id": 201242,
          "name": "逃げ直線○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "running_style": 1, "straight_random": 1 },
    },
    {
      "variants": [
        { "rarity": "double", "id": 201311, "name": "先行直線◎", "targetSpeed": 0.25 },
        {
          "rarity": "normal",
          "id": 201312,
          "name": "先行直線○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "running_style": 2, "straight_random": 1 },
    },
    {
      "variants": [
        { "rarity": "rare", "id": 201383, "name": "鋭脚一閃", "targetSpeed": 0.35 },
        {
          "rarity": "double",
          "id": 201381,
          "name": "差し直線◎",
          "targetSpeed": 0.25,
        },
        { "rarity": "normal", "id": 201382, "name": "差し直線○", "targetSpeed": 0.15 },
      ],
      "duration": 3,
      "conditions": { "running_style": 3, "straight_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103601211,
          "holder": 103601,
          "name": "Lose Myself",
          "targetSpeed": 0.35,
          "speedWithDecel": 0.15,
        },
        { "rarity": "rare", "id": 201453, "name": "月影一閃", "targetSpeed": 0.35 },
        {
          "rarity": "double",
          "id": 201451,
          "name": "追込直線◎",
          "targetSpeed": 0.25,
        },
        { "rarity": "normal", "id": 201452, "name": "追込直線○", "targetSpeed": 0.15 },
      ],
      "duration": 3,
      "conditions": { "running_style": 4, "straight_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102602111,
          "holder": 102602,
          "name": "完璧なエラー対処術",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 102002111,
          "holder": 102002,
          "name": "逃げろ～♪",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 102001211,
          "holder": 102001,
          "name": "脱出大作戦",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 106802111,
          "holder": 106802,
          "name": "未来へ飛び立ちましょう！",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 100202111,
          "holder": 100202,
          "name": "瑠璃色エスケイプ",
          "targetSpeed": 0.45,
        },
        { "rarity": "rare", "id": 200541, "name": "脱出術", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 200542,
          "name": "急ぎ足",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 3,
      "conditions": { "running_style": 1, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103501211,
          "holder": 103501,
          "name": "ウイニングロード",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 102101111,
          "holder": 102101,
          "name": "雷騰雲奔",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 100102111,
          "holder": 100102,
          "name": "真夏の思い切り",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        { "rarity": "rare", "id": 200591, "name": "迅速果断", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 200592,
          "name": "位置取り押し上げ",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 2.4,
      "conditions": { "running_style": 3, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102002211,
          "holder": 102002,
          "name": "レコードランナー",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "rare",
          "id": 201271,
          "name": "トップランナー",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 201272,
          "name": "先頭プライド",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 3,
      "conditions": {
        "running_style": 1,
        "phase_laterhalf_random": 0,
        "accumulatetime": 5,
      },
      "tooltip": "序盤後半のどこかで発動として扱う",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102201111,
          "holder": 102201,
          "name": "ロイヤルスター",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 101501111,
          "holder": 101501,
          "name": "降臨！世紀末覇王！",
          "targetSpeed": 0.45,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 100502211,
          "holder": 100502,
          "name": "煌めきのトップスタァ",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 105801111,
          "holder": 105801,
          "name": "怒涛のスピード",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 106901111,
          "holder": 106901,
          "name": "お花見当たり年！",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 109301111,
          "holder": 109301,
          "name": "K.Speed",
          "invokes": [
            {
              "targetSpeed": 0.35,
              "speedWithDecel": 0.15,
              "conditions": {
                "distance_type": 1,
                "running_style": 2,
                "is_finalcorner_random": 1,
              },
            },
            {
              "targetSpeed": 0.35,
            },
          ],
        },
        {
          "rarity": "rare",
          "id": 200581,
          "name": "スピードスター",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 200582,
          "name": "抜け出し準備",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 1.8,
      "conditions": { "running_style": 2, "is_finalcorner_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103901211,
          "holder": 103901,
          "name": "暴れ龍",
          "targetSpeed": 0.35,
          "acceleration": 0.2,
        },
        {
          "rarity": "evo",
          "id": 102701211,
          "holder": 102701,
          "name": "ライジングバルクアップ",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 101402211,
          "holder": 101402,
          "name": "昇りコンドル",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 105301111,
          "holder": 105301,
          "name": "風紀の登龍門",
          "targetSpeed": 0.35,
          "acceleration": 0.2,
        },
        {
          "rarity": "evo",
          "id": 106102111,
          "holder": 106102,
          "name": "竜の雲を得る如し",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        { "rarity": "rare", "id": 200611, "name": "昇り龍", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 200612,
          "name": "外差し準備",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "running_style": 3, "is_finalcorner_random": 1 },
      "tooltip": "「最終コーナーのどこか」として扱う。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103502111,
          "holder": 103502,
          "name": "千万バリキ！！！",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        { "rarity": "rare", "id": 201411, "name": "百万バリキ", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 201412,
          "name": "十万バリキ",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": { "up_slope_random": 1, "running_style": 3 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104101211,
          "holder": 104101,
          "name": "バクシン的リード！",
          "targetSpeed": 0.45,
        },
        { "rarity": "rare", "id": 200981, "name": "圧倒的リード", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 200982,
          "name": "大きなリード",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": 1, "phase": 1 },
      "tooltip": "中盤に入った瞬間に1位で3馬身リードしていると見なす。見なすな。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106101111,
          "holder": 106101,
          "name": "電撃の撫で切り",
          "targetSpeed": 0.35,
          "acceleration": 0.1,
          "duration": 4,
        },
        {
          "rarity": "rare",
          "id": 200671,
          "name": "電撃の煌めき",
          "targetSpeed": 0.35,
          "acceleration": 0.1,
        },
        {
          "rarity": "normal",
          "id": 200672,
          "name": "詰め寄り",
          "targetSpeed": 0.15,
          "acceleration": 0.05,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": 1, "phase_random": 2, "running_style": [3, 4] },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101002111,
          "holder": 101002,
          "name": "アドベンチャーの先導者",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 101001211,
          "holder": 101001,
          "name": "Frontier Spirit",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 100402211,
          "holder": 100402,
          "name": "お立ち台の支配者",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "rare",
          "id": 200681,
          "name": "マイルの支配者",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 200682,
          "name": "積極策",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": {
        "distance_type": 2,
        "phase_laterhalf_random": 0,
        "running_style": [1, 2],
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102202111,
          "holder": 102202,
          "name": "思い出を力に変えて",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 101001111,
          "holder": 101001,
          "name": "狙い撃ちデス！",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 100401111,
          "holder": 100401,
          "name": "かっ飛ばすわよ！",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 106501111,
          "holder": 106501,
          "name": "マジ爆上げっしょ！",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 107801111,
          "holder": 107801,
          "name": "ウインドシアー",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        { "rarity": "rare", "id": 201051, "name": "ギアチェンジ", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 201052,
          "name": "ギアシフト",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": {
        "distance_type": 2,
        "phase_random": 1,
        "running_style": [1, 2],
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104201211,
          "holder": 104201,
          "name": "パール流ダンスバトル",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 101802111,
          "holder": 101802,
          "name": "女帝の矜持",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 100501111,
          "holder": 100501,
          "name": "輝くトップスタァ",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 110001111,
          "holder": 110001,
          "name": "闘魂注入",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        { "rarity": "rare", "id": 201071, "name": "姉御肌", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 201072,
          "name": "負けん気",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": {
        "distance_type": 2,
        "phase_random": 1,
        "accumulatetime": 5,
      },
      "tooltip": "「中盤のどこか」として扱う。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102701111,
          "holder": 102701,
          "name": "マッスルアンセム",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 102102111,
          "holder": 102102,
          "name": "先達に献ぐ祈りの頌",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 101501211,
          "holder": 101501,
          "name": "歌劇王の行進",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 100901111,
          "holder": 100901,
          "name": "パーフェクトチューン",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 107001211,
          "holder": 107001,
          "name": "一等星のアラベスク",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "rare",
          "id": 200721,
          "name": "キラーチューン",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 200722,
          "name": "テンポアップ",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 2.4,
      "conditions": {
        "distance_type": 3,
        "phase_random": 1,
        "running_style": [1, 2],
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101201111,
          "holder": 101201,
          "name": "勝利への咆哮",
          "targetSpeed": 0.45,
          "acceleration": 0.1,
        },
        {
          "rarity": "evo",
          "id": 107201211,
          "holder": 107201,
          "name": "八重の向こう意気",
          "targetSpeed": 0.45,
          "acceleration": 0.1,
        },
        {
          "rarity": "rare",
          "id": 200731,
          "name": "勝利への執念",
          "targetSpeed": 0.35,
          "acceleration": 0.1,
        },
        {
          "rarity": "normal",
          "id": 200732,
          "name": "食い下がり",
          "targetSpeed": 0.15,
          "acceleration": 0.05,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": 3, "is_finalcorner": 1, "corner": 1 },
      "tooltip": "「最終コーナー即発動」として扱う",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103402111,
          "holder": 103402,
          "name": "扇ノ舞",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 100702111,
          "holder": 100702,
          "name": "神秘体験！ゴルシワープ",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 100602211,
          "holder": 100602,
          "name": "クリスマスの奇跡体験",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 105602211,
          "holder": 105602,
          "name": "ばっちり開運体験！",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 103602211,
          "holder": 103602,
          "name": "刑苦",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        { "rarity": "rare", "id": 200751, "name": "内的体験", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 200752,
          "name": "内弁慶",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 3,
      "conditions": {
        "distance_type": 4,
        "is_finalcorner": 1,
        "corner": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102001111,
          "holder": 102001,
          "name": "ファストリトリーブ",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 102001121,
          "holder": 102001,
          "name": "大物狙いの心得",
          "heal": 750,
          "conditions": {
            "distance_type": 3,
            "phase_random": 1,
          },
        },
        {
          "rarity": "evo",
          "id": 101301111,
          "holder": 101301,
          "name": "メジロの心得",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 106401211,
          "holder": 106401,
          "name": "神逃げの心得",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        { "rarity": "rare", "id": 201191, "name": "先陣の心得", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 201192,
          "name": "リードキープ",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": {
        "distance_type": 4,
        "phase_random": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106401111,
          "holder": 106401,
          "name": "パないっしょ？",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "rare",
          "id": 201662,
          "name": "お先に失礼っ！",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 201661,
          "name": "遊びはおしまいっ！",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "cd": 30,
      "conditions": {
        "phase_random": 1,
        "accumulatetime": 10,
      },
      "tooltip": "「中盤のどこかで発動」として扱う。",
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 201652,
          "name": "いいとこ入った！",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 201651,
          "name": "スリップストリーム",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": {
        "phase_random": 1,
        "accumulatetime": 10,
      },
      "tooltip": "「中盤のどこかで発動」として扱う。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103301211,
          "holder": 103301,
          "name": "己身焦がすほうき星",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 101201211,
          "holder": 101201,
          "name": "怒濤のポロロッカ",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 108401111,
          "holder": 108401,
          "name": "嵐を呼ぶ破壊神",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "rare",
          "id": 200631,
          "name": "疾風怒濤",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 200632,
          "name": "仕掛け抜群",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": {
        "running_style": 4,
        "phase_firsthalf_random": 3,
        "is_lastspurt": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100103111,
          "holder": 100103,
          "name": "怒涛の出陣",
          "targetSpeed": 0.35,
          "acceleration": 0.1,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 105602111,
          "holder": 105602,
          "name": "怒涛の超幸運パワー！",
          "targetSpeed": 0.35,
          "acceleration": 0.1,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 106201211,
          "holder": 106201,
          "name": "えい！えい！むん！",
          "targetSpeed": 0.35,
          "acceleration": 0.1,
          "heal": 150,
        },
        {
          "rarity": "rare",
          "id": 201211,
          "name": "怒涛の追い上げ",
          "targetSpeed": 0.35,
          "acceleration": 0.1,
        },
        {
          "rarity": "normal",
          "id": 201212,
          "name": "追い上げ",
          "targetSpeed": 0.15,
          "acceleration": 0.05,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": 4, "phase_random": 2 },
      "tooltip": "「終盤前半のどこか」として扱う",
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 201081,
          "name": "スピードグリード",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 201082,
          "name": "スピードイーター",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": {
        "distance_type": 2,
        "phase_random": 1,
        "running_style": [1, 2],
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102102211,
          "holder": 102102,
          "name": "さあ、ウチとやろうや！",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 101902111,
          "holder": 101902,
          "name": "超特急入稿！",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 100102211,
          "holder": 100102,
          "name": "真夏の総大将",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 107201111,
          "holder": 107201,
          "name": "昂る焔",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "rare",
          "id": 200461,
          "name": "アガッてきた！",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 200462,
          "name": "ペースアップ",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 1.8,
      "cd": 30,
      "tooltip": "「中盤のどこかで発動」として扱う。",
      "conditions": { "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104602111,
          "holder": 104602,
          "name": "夢に向かって急上昇！",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 104601211,
          "holder": 104601,
          "name": "ウマドルパワー急上昇↑↑",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 109901211,
          "holder": 109901,
          "name": "観光大使の面目躍如！",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "rare",
          "id": 201671,
          "name": "チャート急上昇！",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 201672,
          "name": "レコメンド",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 2.4,
      "conditions": { "ground_type": 2, "phase_random": 1 },
      "tooltip": "「中盤のどこか」として扱う。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103701211,
          "holder": 103701,
          "name": "誇りを懸けて",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 102302211,
          "holder": 102302,
          "name": "全力サンタクロース",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 101601211,
          "holder": 101601,
          "name": "餓狼牙",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 100101211,
          "holder": 100101,
          "name": "夢叶える末脚",
          "targetSpeed": 0.45,
        },
        { "rarity": "rare", "id": 200511, "name": "全身全霊", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 200512,
          "name": "末脚",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": { "phase_firsthalf_random": 3, "is_lastspurt": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103401111,
          "holder": 103401,
          "name": "火消の梯子登り",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 102101211,
          "holder": 102101,
          "name": "尻尾の有頂天",
          "targetSpeed": 0.45,
        },
        { "rarity": "rare", "id": 201612, "name": "尻尾の滝登り", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 201611,
          "name": "尻尾上がり",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "check": "function () {
        return thiz.skillTriggerCount[1] >= 3;
      }",
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 210011,
          "name": "アオハル燃焼・速",
          "targetSpeed": 0.4025,
        },
        {
          "rarity": "normal",
          "id": 210012,
          "name": "アオハル点火・速",
          "targetSpeed": 0.1725,
        },
      ],
      "duration": 1.8,
      "tooltip": "効果1.15倍として扱う",
      "conditions": { "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103502211,
          "holder": 103502,
          "name": "きっと飛べる！",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 107101211,
          "holder": 107101,
          "name": "刹那への覚悟",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 110501111,
          "holder": 110501,
          "name": "銀河のその先へ、あなたと",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 107001111,
          "holder": 107001,
          "name": "揺るがぬアスター",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "rare",
          "id": 201701,
          "name": "決死の覚悟",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 201702,
          "name": "ありったけ",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": {
        "phase": ">=2",
        "distance_type": 3,
        "is_last_straight": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101502211,
          "holder": 101502,
          "name": "ボクは遅れずにやってくる！",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 101303111,
          "holder": 101303,
          "name": "夏の名優",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 107701211,
          "holder": 107701,
          "name": "正道",
          "invokes": [
            {
              "invokeNo": 1,
              "targetSpeed": 0.35,
              "duration": 2.4,
              "conditions": {
                "distance_type": 4,
                "phase_laterhalf_random": 1,
              },
            },
            {
              "invokeNo": 2,
              "acceleration": 0.2,
              "duration": 0.9,
              "conditions": {
                "distance_type": 4,
                "is_lastspurt": 1,
              },
            },
          ],
        },
        { "rarity": "rare", "id": 202011, "name": "真打", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 202012,
          "name": "影打",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": { "distance_type": 4, "phase_laterhalf_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101202111,
          "holder": 101202,
          "name": "熱烈エンゲージ！",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "rare",
          "id": 202022,
          "name": "強攻策",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 202021,
          "name": "早仕掛け",
          "targetSpeed": 0.05,
        },
      ],
      "duration": 4,
      "conditions": { "running_style": 4, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100103211,
          "holder": 100103,
          "name": "日之本一のド根性",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "rare",
          "id": 202061,
          "name": "日本一のウマ娘",
          "targetSpeed": 0.35,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": 4, "is_finalcorner_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101901111,
          "holder": 101901,
          "name": "限界ウママニア",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        { "rarity": "rare", "id": 201592, "name": "ウママニア", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 201591,
          "name": "ウマ好み",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 3,
      "conditions": { "accumulatetime": 5 },
      "tooltip": "開始5秒で即発動扱い",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103601111,
          "holder": 103601,
          "name": "アップリフティング",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 100702211,
          "holder": 100702,
          "name": "イカ揚げ、いかがですか！？",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 105501111,
          "holder": 105501,
          "name": "ワクワク☆マーベラスゾーン",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "rare",
          "id": 202101,
          "name": "高揚感",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202102,
          "name": "前のめり",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 2.4,
      "conditions": { "distance_type": 3, "phase_random": 1 },
      "tooltip": ">=50%(5～9位)",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105301211,
          "holder": 105301,
          "name": "押忍ッ！気合十分ッス！",
          "targetSpeed": 0.35,
          "acceleration": 0.2,
        },
        { "rarity": "rare", "id": 202111, "name": "破竹の勢い", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 202112,
          "name": "気合十分",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 1.8,
      "conditions": { "distance_type": 2, "is_finalcorner_random": 1 },
      "tooltip": ">=50%(5～9位)",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103101211,
          "holder": 103101,
          "name": "荒ぶる風神",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        { "rarity": "rare", "id": 202131, "name": "荒ぶる旋風", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 202132,
          "name": "気迫を込めて",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 1.8,
      "conditions": { "distance_type": 3, "phase_random": 1, "running_style": [1, 2] },
      "tooltip": "1～3位。デバフは金0.15/白0.035。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102702211,
          "holder": 102702,
          "name": "至高の勝利を召し上がれ",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "evo",
          "id": 105102211,
          "holder": 105102,
          "name": "ヴェール揺らす春疾風",
          "targetSpeed": 0.35,
          "acceleration": 0.2,
          "conditions": {
            "distance_type": [1, 2],
            "down_slope_random": 1,
          },
        },
        {
          "rarity": "rare",
          "id": 202171,
          "name": "至高のダウンヒラー",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202172,
          "name": "下り坂巧者",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 2.4,
      "conditions": { "down_slope_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 108701211,
          "holder": 108701,
          "name": "記憶に刻む足取り",
          "targetSpeed": 0.35,
          "acceleration": 0.2,
        },
        {
          "rarity": "evo",
          "id": 104102111,
          "holder": 104102,
          "name": "猪突猛進！バクシンロード！",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "rare",
          "id": 202041,
          "name": "意気衝天",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202042,
          "name": "軽い足取り",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": { "distance_type": 1, "phase_laterhalf_random": 1 },
      "tooltip": "1～5位",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 109801111,
          "holder": 109801,
          "name": "コパッと開運！",
          "targetSpeed": 0.45,
        },
        { "rarity": "rare", "id": 202261, "name": "勝利の機運", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 202262,
          "name": "明るい兆し",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": { "ground_type": 2, "phase_laterhalf_random": 1 },
      "tooltip": "1～5位",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101902211,
          "holder": 101902,
          "name": "尊みを求めてふっかーーつ！",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        { "rarity": "rare", "id": 202271, "name": "捲土重来", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 202272,
          "name": "盛り返し",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 2.4,
      "conditions": { "ground_type": 2, "phase_random": 1 },
      "tooltip": "6～9位。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104301111,
          "holder": 104301,
          "name": "全速前進なのだ！",
          "targetSpeed": 0.45,
        },
        { "rarity": "rare", "id": 202281, "name": "全速前進！", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 202282,
          "name": "まっしぐら",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "ground_type": 2, "phase_random": 3, "is_lastspurt": 1 },
      "tooltip": "1～5位。",
    },
    {
      "variants": [
        { "rarity": "rare", "id": 202291, "name": "明鏡止水", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 202292,
          "name": "冴える思考",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 1.8,
      "conditions": {
        "ground_type": 2,
        "distance_rate": "<=42",
        "phase": 1,
        "activate_count_heal": 1,
      },
      "tooltip": "6～9位。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 110001211,
          "holder": 110001,
          "name": "女神の砂浴び",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        { "rarity": "rare", "id": 202301, "name": "優雅な砂浴び", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 202302,
          "name": "砂浴び○",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "ground_type": 2, "accumulatetime": 5 },
      "tooltip": "開始5秒で即発動扱い。逃げで使う場合は自己責任で。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103202211,
          "holder": 103202,
          "name": "眩耀のルクシオン",
          "targetSpeed": 0.45,
          "tooltip": "0.45扱い",
        },
        {
          "rarity": "rare",
          "id": 202371,
          "name": "アンストッパブル",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202372,
          "name": "攻めの姿勢",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": { "phase_random": 1, "running_style": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105002111,
          "holder": 105002,
          "name": "見せつけてやる！",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "rare",
          "id": 202381,
          "name": "ブレイクスルー",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202382,
          "name": "打開策",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": { "phase_random": 1, "running_style": 4 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 107801211,
          "holder": 107801,
          "name": "神渡し",
          "targetSpeed": 0.35,
        },
        { "rarity": "rare", "id": 202411, "name": "風雲の志", "targetSpeed": 0.25 },
        {
          "rarity": "normal",
          "id": 202412,
          "name": "向上心",
          "targetSpeed": 0.05,
        },
      ],
      "duration": 4,
      "conditions": {
        "running_style": 2,
        "phase": 1,
        "distance_rate_random": [60, 66],
      },
      "tooltip": "60%～66%のランダム区間発動扱い",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100902211,
          "holder": 100902,
          "name": "開かれる夢の扉",
          "targetSpeed": 0.55,
        },
        {
          "rarity": "evo",
          "id": 110401211,
          "holder": 110401,
          "name": "逃げっ切りの1人旅",
          "targetSpeed": 0.55,
          "conditions": {
            "running_style": 1,
            "is_finalcorner": 1,
            "corner": 0,
            "track_id": 10006,
          },
        },
        { "rarity": "rare", "id": 202461, "name": "踏ませぬ影", "targetSpeed": 0.45 },
        {
          "rarity": "normal",
          "id": 202462,
          "name": "粘り腰",
          "targetSpeed": 0.25,
        },
      ],
      "duration": 2.4,
      "conditions": { "running_style": 1, "is_finalcorner": 1, "corner": 0 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100802211,
          "holder": 100802,
          "name": "最速のトップギア",
          "targetSpeed": 0.55,
        },
        {
          "rarity": "rare",
          "id": 202451,
          "name": "トップギア",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "normal",
          "id": 202452,
          "name": "キレる脚",
          "targetSpeed": 0.25,
        },
      ],
      "duration": 2.4,
      "tooltip": "先頭から4バ身以内にいる必要がある",
      "conditions": { "running_style": 3, "is_finalcorner": 1, "corner": 0 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101602111,
          "holder": 101602,
          "name": "BLAZING WOLF",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        { "rarity": "rare", "id": 202471, "name": "猛追", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 202472,
          "name": "食らいつき",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": { "running_style": [2, 3], "distance_rate": ">=50" },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106501211,
          "holder": 106501,
          "name": "とりまやったれ～！",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        { "rarity": "rare", "id": 202501, "name": "遮二無二", "targetSpeed": 0.35 },
        {
          "rarity": "normal",
          "id": 202502,
          "name": "向こう見ず",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "tooltip": "順位<=50%",
      "conditions": { "distance_type": 2, "phase_laterhalf_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101802211,
          "holder": 101802,
          "name": "上弦のソムリエール",
          "acceleration": 0.4,
          "speedWithDecel": 0.15,
          "type": "composite",
        },
        {
          "rarity": "evo",
          "id": 100601211,
          "holder": 100601,
          "name": "怪物的コーナリング",
          "acceleration": 0.4,
          "heal": 150,
        },
        {
          "rarity": "rare",
          "id": 200341,
          "name": "曲線のソムリエ",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 200342,
          "name": "コーナー加速○",
          "acceleration": 0.2,
        },
      ],
      "type": "acceleration",
      "duration": 3,
      "cd": 30,
      "conditions": { "all_corner_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 200371,
          "name": "一陣の風",
          "acceleration": 0.4,
          "targetSpeed": 0.15,
        },
        { "rarity": "normal", "id": 200372, "name": "直線加速", "acceleration": 0.2 },
      ],
      "duration": 3,
      "cd": 30,
      "conditions": { "straight_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102601111,
          "holder": 102601,
          "name": "クロックアップ",
          "acceleration": 0.5,
        },
        {
          "rarity": "evo",
          "id": 106601211,
          "holder": 106601,
          "name": "ターボについてこーい！",
          "acceleration": 0.5,
        },
        {
          "rarity": "evo",
          "id": 106801111,
          "holder": 106801,
          "name": "祭りだワッショイ！",
          "acceleration": 0.5,
        },
        {
          "rarity": "evo",
          "id": 106802211,
          "holder": 106802,
          "name": "必勝の前口上！",
          "acceleration": 0.5,
        },
        {
          "rarity": "evo",
          "id": 108701111,
          "holder": 108701,
          "name": "必勝のピッチアップ",
          "acceleration": 0.4,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 110401111,
          "holder": 110401,
          "name": "してやったり！",
          "acceleration": 0.5,
        },
        { "rarity": "rare", "id": 200531, "name": "先手必勝", "acceleration": 0.4 },
        {
          "rarity": "normal",
          "id": 200532,
          "name": "先駆け",
          "acceleration": 0.2,
        },
      ],
      "conditions": {
        "running_style": 1,
        "phase": 0,
      },
      "duration": 1.2,
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100201211,
          "holder": 100201,
          "name": "異次元の逃亡者",
          "acceleration": 0.4,
          "speedWithDecel": 0.15,
        },
        { "rarity": "rare", "id": 200551, "name": "逃亡者", "acceleration": 0.4 },
        {
          "rarity": "normal",
          "id": 200552,
          "name": "押し切り準備",
          "acceleration": 0.2,
        },
      ],
      "duration": 3,
      "conditions": { "running_style": 1, "is_finalcorner_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101101211,
          "holder": 101101,
          "name": "不退転の意思",
          "acceleration": 0.4,
        },
        {
          "rarity": "evo",
          "id": 104001211,
          "holder": 104001,
          "name": "ランウェイの主役",
          "acceleration": 0.4,
        },
        {
          "rarity": "evo",
          "id": 104801111,
          "holder": 104801,
          "name": "ノってこ！",
          "acceleration": 0.4,
          "heal": 150,
          "conditions": { "running_style": [2, 3], "phase_firsthalf_random": 2 },
        },
        {
          "rarity": "evo",
          "id": 105302111,
          "holder": 105302,
          "name": "熱血進化ライディング！",
          "variants": [
            {
              "acceleration": 0.4,
              "duration": 1.8,
              "conditions": { "running_style": 3, "phase_firsthalf_random": 2 },
            },
            {
              "conditions": { "running_style": 3, "remain_distance": [199, 201] },
              "targetSpeed": 0.15,
            },
          ],
        },
        {
          "rarity": "evo",
          "id": 108302211,
          "holder": 108302,
          "name": "Service: Transit",
          "acceleration": 0.4,
          "duration": 4,
          "conditions": {
            "distance_type": 3,
            "running_style": 3,
            "phase_firsthalf_random": 2,
          },
        },
        {
          "rarity": "rare",
          "id": 200601,
          "name": "乗り換え上手",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 200602,
          "name": "差し切り体勢",
          "acceleration": 0.2,
        },
      ],
      "duration": 1.8,
      "conditions": { "running_style": 3, "phase_firsthalf_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103402211,
          "holder": 103402,
          "name": "大見得切り",
          "acceleration": 0.4,
        },
        {
          "rarity": "evo",
          "id": 105001211,
          "holder": 105001,
          "name": "駆り立てる想い",
          "acceleration": 0.4,
        },
        {
          "rarity": "evo",
          "id": 105701211,
          "holder": 105701,
          "name": "弾む大地",
          "acceleration": 0.4,
        },
        {
          "rarity": "evo",
          "id": 100703111,
          "holder": 100703,
          "name": "押し寄せるジュテーム",
          "invokes": [
            {
              "invokeNo": 1,
              "acceleration": 0.4,
              "duration": 0.9,
            },
            {
              "invokeNo": 2,
              "targetSpeed": 0.15,
              "duration": 3,
              "conditions": { "running_style": 4, "is_last_straight": 1 },
            },
          ],
        },
        { "rarity": "rare", "id": 200641, "name": "迫る影", "acceleration": 0.4 },
        {
          "rarity": "normal",
          "id": 200642,
          "name": "直線一気",
          "acceleration": 0.2,
        },
      ],
      "duration": 0.9,
      "conditions": { "running_style": 4, "is_lastspurt": 1, "corner": 0 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103802211,
          "holder": 103802,
          "name": "#ビューティースプリント",
          "acceleration": 0.4,
        },
        {
          "rarity": "evo",
          "id": 104101111,
          "holder": 104101,
          "name": "バクシン的スプリント！",
          "acceleration": 0.4,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 105302211,
          "holder": 105302,
          "name": "必殺！爆速スプリント！",
          "variants": [
            {
              "acceleration": 0.4,
              "conditions": { "distance_type": 1, "phase_random": 2 },
            },
            {
              "conditions": { "distance_type": 1, "remain_distance": [199, 201] },
              "targetSpeed": 0.15,
            },
          ],
        },
        {
          "rarity": "rare",
          "id": 200651,
          "name": "スプリントターボ",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 200652,
          "name": "スプリントギア",
          "acceleration": 0.2,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": 1, "phase_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101402111,
          "holder": 101402,
          "name": "豪勇無双",
          "acceleration": 0.5,
        },
        {
          "rarity": "evo",
          "id": 100801211,
          "holder": 100801,
          "name": "抜群の豪脚",
          "acceleration": 0.5,
        },
        { "rarity": "rare", "id": 200701, "name": "豪脚", "acceleration": 0.4 },
        {
          "rarity": "normal",
          "id": 200702,
          "name": "上昇気流",
          "acceleration": 0.2,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": 2, "phase_random": 2 },
    },
    {
      "variants": [
        { "rarity": "rare", "id": 200991, "name": "プランX", "acceleration": 0.4 },
        {
          "rarity": "normal",
          "id": 200992,
          "name": "善後策",
          "acceleration": 0.2,
        },
      ],
      "duration": 3,
      "conditions": {
        "distance_type": 1,
        "phase_laterhalf_random": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100401211,
          "holder": 100401,
          "name": "紅蓮のオーバーレブ",
          "targetSpeed": 0.45,
        },
      ],
      "duration": 3,
      "tooltip": "50%突入時1位の即発動扱い。",
      "conditions": {
        "distance_type": 2,
        "distance_rate": ">=50",
      },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 201061,
          "name": "アクセル全開！",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 201062,
          "name": "アクセラレーション",
          "acceleration": 0.2,
        },
      ],
      "duration": 3,
      "tooltip": "「50％以降のどこかで発動」として扱う。",
      "conditions": { "distance_type": 2, "distance_rate_after_random": 50 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100301211,
          "holder": 100301,
          "name": "帝王ステップ",
          "acceleration": 0.3,
          "targetSpeed": 0.25,
        },
        {
          "rarity": "rare",
          "id": 201131,
          "name": "ライトニングステップ",
          "acceleration": 0.3,
        },
        {
          "rarity": "normal",
          "id": 201132,
          "name": "イナズマステップ",
          "acceleration": 0.2,
        },
      ],
      "duration": 4,
      "conditions": { "distance_type": 3, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 201291,
          "name": "再燃焼",
          "acceleration": 0.4,
          "heal": -200,
        },
        {
          "rarity": "normal",
          "id": 201292,
          "name": "二の矢",
          "acceleration": 0.2,
          "heal": -200,
        },
      ],
      "duration": 3,
      "conditions": { "running_style": 1, "phase_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100301111,
          "holder": 100301,
          "name": "天才的技巧",
          "acceleration": 0.3,
          "targetSpeed": 0.25,
        },
        { "rarity": "rare", "id": 201331, "name": "技巧派", "acceleration": 0.3 },
        {
          "rarity": "normal",
          "id": 201332,
          "name": "巧みなステップ",
          "acceleration": 0.2,
        },
      ],
      "duration": 3,
      "conditions": { "running_style": 2, "accumulatetime": 20 },
      "tooltip": "「スタート後20秒で発動」として扱う。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102201211,
          "holder": 102201,
          "name": "天空落とし",
          "acceleration": 0.4,
        },
        {
          "rarity": "evo",
          "id": 102702111,
          "holder": 102702,
          "name": "夢叶える決意を授けましょう",
          "acceleration": 0.4,
        },
        {
          "rarity": "evo",
          "id": 102901211,
          "holder": 102901,
          "name": "雪国仕込みの直滑降",
          "acceleration": 0.3,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 103001111,
          "holder": 103001,
          "name": "決意のヒーロー",
          "acceleration": 0.3,
          "speedWithDecel": 0.15,
        },
        {
          "rarity": "evo",
          "id": 105101211,
          "holder": 105101,
          "name": "大輪の決意",
          "acceleration": 0.4,
        },
        { "rarity": "rare", "id": 201341, "name": "決意の直滑降", "acceleration": 0.3 },
        {
          "rarity": "normal",
          "id": 201342,
          "name": "直滑降",
          "acceleration": 0.2,
        },
      ],
      "duration": 3,
      "conditions": { "down_slope_random": 1, "running_style": 2 },
    },
    {
      "variants": [
        { "rarity": "normal", "id": 201581, "name": "登山家", "acceleration": 0.2 },
      ],
      "duration": 3,
      "conditions": { "up_slope_random": 1 },
    },
    {
      "variants": [
        { "rarity": "rare", "id": 201361, "name": "くじけぬ精神", "acceleration": 0.3 },
        { "rarity": "normal", "id": 201362, "name": "まき直し", "acceleration": 0.2 },
      ],
      "duration": 3,
      "conditions": {
        "running_style": 2,
        "phase_random": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103501111,
          "holder": 103501,
          "name": "たゆまぬ努力",
          "acceleration": 0.3,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 105202211,
          "holder": 105202,
          "name": "がんばるもん！",
          "acceleration": 0.3,
          "targetSpeed": 0.15,
        },
        { "rarity": "rare", "id": 201401, "name": "努力家", "acceleration": 0.3 },
        {
          "rarity": "normal",
          "id": 201402,
          "name": "がんばり屋",
          "acceleration": 0.2,
        },
      ],
      "duration": 4,
      "conditions": {
        "running_style": 3,
        "phase_random": 1,
      },
      "tooltip": "「中盤のどこか」として扱う。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100802111,
          "holder": 100802,
          "name": "ぶっ差してやるぜ！",
          "acceleration": 0.4,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 102401211,
          "holder": 102401,
          "name": "I copy!",
          "acceleration": 0.4,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 102901111,
          "holder": 102901,
          "name": "憧れのシチーガール",
          "acceleration": 0.4,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 103901111,
          "holder": 103901,
          "name": "ノンストッププリンセス",
          "acceleration": 0.5,
        },
        {
          "rarity": "evo",
          "id": 106002211,
          "holder": 106002,
          "name": "ノンストップエール",
          "acceleration": 0.4,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 105501211,
          "holder": 105501,
          "name": "進め！世界マーベラス計画☆",
          "acceleration": 0.4,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "rare",
          "id": 200491,
          "name": "ノンストップガール",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 200492,
          "name": "垂れウマ回避",
          "acceleration": 0.2,
        },
      ],
      "type": "acceleration",
      "duration": 3,
      "conditions": { "is_lastspurt": 1 },
      "tooltip": "スパート即発動として扱う。実際に発動するかどうかは自己判断で。",
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 201602,
          "name": "盤石の構え",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 201601,
          "name": "地固め",
          "acceleration": 0.2,
        },
      ],
      "duration": 3,
      "conditions": {
        "activate_count_start": 3,
      },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 210031,
          "name": "アオハル燃焼・力",
          "acceleration": 0.44,
        },
        {
          "rarity": "normal",
          "id": 210032,
          "name": "アオハル点火・力",
          "acceleration": 0.22,
        },
      ],
      "duration": 1.2,
      "conditions": { "phase_random": 2 },
      "tooltip": "効果1.1倍として扱う",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101901211,
          "holder": 101901,
          "name": "最前列は譲れない！",
          "acceleration": 0.3,
          "duration": 4,
        },
        {
          "rarity": "rare",
          "id": 201681,
          "name": "狙うは最前列！",
          "acceleration": 0.3,
        },
        {
          "rarity": "normal",
          "id": 201682,
          "name": "前列狙い",
          "acceleration": 0.2,
        },
      ],
      "duration": 3,
      "conditions": { "ground_type": 2, "phase_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102202211,
          "holder": 102202,
          "name": "モリガンの矛戟",
          "acceleration": 0.4,
        },
        {
          "rarity": "evo",
          "id": 101002211,
          "holder": 101002,
          "name": "真剣勝負デス！",
          "acceleration": 0.4,
        },
        {
          "rarity": "evo",
          "id": 105802111,
          "holder": 105802,
          "name": "ライバルがいるから！",
          "acceleration": 0.4,
          "heal": 150,
        },
        {
          "rarity": "evo",
          "id": 109301211,
          "holder": 109301,
          "name": "命の火花",
          "invokes": [
            {
              "invokeNo": 1,
              "acceleration": 0.4,
            },
            {
              "invokeNo": 2,
              "targetSpeed": 0.15,
              "duration": 3,
              "conditions": {
                "running_style": 2,
                "remain_distance": [199, 201],
              },
            },
          ],
        },
        { "rarity": "rare", "id": 201901, "name": "鍔迫り合い", "acceleration": 0.4 },
        {
          "rarity": "normal",
          "id": 201902,
          "name": "真っ向勝負",
          "acceleration": 0.2,
        },
      ],
      "type": "acceleration",
      "duration": 1.8,
      "conditions": { "running_style": 2, "phase_firsthalf_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105002211,
          "holder": 105002,
          "name": "起死回生の出力",
          "acceleration": 0.4,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 105902211,
          "holder": 105902,
          "name": "起死回生の一歩",
          "acceleration": 0.4,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 106202211,
          "holder": 106202,
          "name": "大どんでんがえし",
          "acceleration": 0.4,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 104802111,
          "holder": 104802,
          "name": "ワンチャンまくったる！",
          "acceleration": 0.4,
          "targetSpeed": 0.25,
          "duration": 1.8,
          "conditions": {
            "distance_type": 3,
            "is_finalcorner_random": 1,
            "track_id": 10006,
          },
        },
        {
          "rarity": "evo",
          "id": 106703111,
          "holder": 106703,
          "name": "蒼色革命",
          "acceleration": 0.4,
          "duration": 4,
        },
        {
          "rarity": "rare",
          "id": 202081,
          "name": "起死回生",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 202082,
          "name": "ワンチャンス",
          "acceleration": 0.2,
        },
      ],
      "duration": 1.8,
      "conditions": { "distance_type": 3, "phase_firsthalf_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104602211,
          "holder": 104602,
          "name": "絶対的センター！",
          "acceleration": 0.5,
        },
        {
          "rarity": "rare",
          "id": 202311,
          "name": "目指せセンター！",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 202312,
          "name": "意気込み十分",
          "acceleration": 0.2,
        },
      ],
      "duration": 3,
      "conditions": { "ground_type": 2, "phase_random": 2 },
      "tooltip": "1～5位。",
    },
    {
      "variants": [
        { "rarity": "rare", "id": 202321, "name": "爆走モード！", "acceleration": 0.4 },
        {
          "rarity": "normal",
          "id": 202322,
          "name": "急浮上",
          "acceleration": 0.2,
        },
      ],
      "duration": 3,
      "conditions": { "ground_type": 2, "phase_random": 2 },
      "tooltip": "5～9位。",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 108501211,
          "holder": 108501,
          "name": "常に最たる輝きを",
          "acceleration": 0.4,
          "tooltip": "速度デバフは-0.15。",
        },
        {
          "rarity": "rare",
          "id": 202401,
          "name": "電光石火",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 202402,
          "name": "一足飛び",
          "acceleration": 0.2,
        },
      ],
      "duration": 2,
      "conditions": { "distance_type": [1, 2], "phase": ">=2" },
      "tooltip": "5～9位、即発動扱い",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101602211,
          "holder": 101602,
          "name": "渇望する怪物",
          "acceleration": 0.5,
        },
        { "rarity": "rare", "id": 202481, "name": "怪物", "acceleration": 0.4 },
        {
          "rarity": "normal",
          "id": 202482,
          "name": "本領発揮",
          "acceleration": 0.2,
        },
      ],
      "duration": 1.2,
      "conditions": { "distance_type": 4, "running_style": 2, "phase": 2 },
      "tooltip": "先頭から4バ身以内の即発動扱い",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101202211,
          "holder": 101202,
          "name": "闘魂入刀！",
          "acceleration": 0.4,
          "duration": 4,
          "conditions": {
            "running_style": 4,
            "distance_type": 3,
            "phase_firsthalf_random": 2,
          },
        },
        {
          "rarity": "rare",
          "id": 202491,
          "name": "抜群の切れ味",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 202492,
          "name": "切れ味",
          "acceleration": 0.2,
        },
      ],
      "duration": 1.2,
      "conditions": { "running_style": 4, "phase_firsthalf_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 210041,
          "name": "アオハル燃焼・根",
          "targetSpeed": 0.275,
          "acceleration": 0.33,
        },
        {
          "rarity": "normal",
          "id": 210042,
          "name": "アオハル点火・根",
          "targetSpeed": 0.055,
          "acceleration": 0.11,
        },
      ],
      "duration": 1.8,
      "conditions": { "phase_random": 2 },
      "tooltip": "効果1.1倍として扱う",
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 210061,
          "name": "一番星",
          "targetSpeed": 0.3,
          "acceleration": 0.36,
          "heal": 420,
        },
        {
          "rarity": "normal",
          "id": 210062,
          "name": "綺羅星",
          "targetSpeed": 0.06,
          "acceleration": 0.12,
          "heal": 60,
        },
      ],
      "duration": 1.2,
      "tooltip": "効果1.2倍(25戦↑)として扱う",
      "conditions": { "distance_rate_after_random": 50 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106902211,
          "holder": 106902,
          "name": "負けたくない！",
          "targetSpeed": 0.35,
          "heal": 550,
        },
        {
          "rarity": "evo",
          "id": 108302111,
          "holder": 108302,
          "name": "Burn Down",
          "targetSpeed": 0.35,
          "heal": 350,
          "tooltip": "チームバフ効果は実装のしようがない",
        },
        {
          "rarity": "rare",
          "id": 202091,
          "name": "気炎万丈",
          "targetSpeed": 0.35,
          "heal": 350,
        },
        {
          "rarity": "normal",
          "id": 202092,
          "name": "闘争心",
          "targetSpeed": 0.15,
          "heal": 50,
        },
      ],
      "duration": 2.4,
      "conditions": { "distance_type": 3, "phase_random": 1 },
      "tooltip": "中盤ランダム発動扱い",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105902111,
          "holder": 105902,
          "name": "もっと冒険してみたい！",
          "targetSpeed": 0.35,
          "acceleration": 0.1,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 104802211,
          "holder": 104802,
          "name": "あたししか勝たん！",
          "targetSpeed": 0.35,
          "acceleration": 0.2,
          "duration": 2.4,
          "conditions": {
            "running_style": 3,
            "distance_type": 3,
            "is_finalcorner_random": 1,
            "track_id": 10006,
          },
        },
        {
          "rarity": "rare",
          "id": 202121,
          "name": "大胆不敵",
          "targetSpeed": 0.35,
          "acceleration": 0.1,
        },
        {
          "rarity": "normal",
          "id": 202122,
          "name": "恐れぬ心",
          "targetSpeed": 0.15,
          "acceleration": 0.05,
        },
      ],
      "conditions": { "running_style": 3, "distance_rate_after_random": 50 },
      "duration": 2.4,
      "tooltip": ">=40% <=70%(4～6位)",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "holder": 107601,
          "id": 202151,
          "name": "桜前線進行中！",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "rare",
          "id": 202151,
          "name": "勇往邁進",
          "targetSpeed": 0.45,
          "heal": -200,
        },
        {
          "rarity": "normal",
          "id": 202152,
          "name": "フルスロットル",
          "targetSpeed": 0.25,
          "heal": -200,
        },
      ],
      "type": "speed",
      "conditions": { "running_style": 3, "phase_random": 1 },
      "duration": 2.4,
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 210071,
          "name": "キミと勝ちたい",
          "targetSpeed": 0.42,
          "acceleration": 0.24,
        },
        {
          "rarity": "normal",
          "id": 210072,
          "name": "夢の途中",
          "targetSpeed": 0.18,
          "acceleration": 0.084,
        },
      ],
      "duration": 1.2,
      "tooltip": "1～6位。ファン数16万以上の場合。",
      "conditions": { "distance_rate_after_random": 50 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106601111,
          "holder": 106601,
          "name": "出力1000万%！！",
          "targetSpeed": 0.55,
          "heal": -400,
        },
        {
          "rarity": "rare",
          "id": 202391,
          "name": "出力1000％！",
          "targetSpeed": 0.45,
          "heal": -400,
        },
        {
          "rarity": "normal",
          "id": 202392,
          "name": "しゃかりき",
          "targetSpeed": 0.25,
          "heal": -400,
        },
      ],
      "type": "speed",
      "duration": 2.7,
      "conditions": { "phase_firsthalf_random": 1, "running_style": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106702111,
          "holder": 106702,
          "name": "無我の境地",
          "acceleration": 0.4,
          "heal": -50,
        },
        {
          "rarity": "evo",
          "id": 108301111,
          "holder": 108301,
          "name": "質実剛健",
          "acceleration": 0.4,
          "invokes": [
            {
              "heal": 150,
              "conditions": {
                "distance_type": 4,
                "running_style": 3,
                "is_lastspurt": 1,
                "track_id": 10005,
              },
            },
            {
              "heal": -200,
              "conditions": {
                "distance_type": 4,
                "running_style": 3,
                "is_lastspurt": 1,
              },
            },
          ],
        },
        {
          "rarity": "rare",
          "id": 202421,
          "name": "無我夢中",
          "acceleration": 0.4,
          "heal": -200,
        },
        {
          "rarity": "normal",
          "id": 202422,
          "name": "がむしゃら",
          "acceleration": 0.2,
          "heal": -200,
        },
      ],
      "type": "acceleration",
      "duration": 1.5,
      "conditions": { "distance_type": 4, "running_style": 3, "is_lastspurt": 1 },
      "tooltip": "4-9位",
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 202431,
          "name": "快進撃",
          "targetSpeed": 0.25,
          "acceleration": 0.3,
        },
        {
          "rarity": "normal",
          "id": 202432,
          "name": "確かな足取り",
          "targetSpeed": 0.05,
          "acceleration": 0.1,
        },
      ],
      "duration": 3,
      "tooltip": "発動地点に先頭から４馬身以内にいる必要がある",
      "conditions": {
        "distance_type": 3,
        "running_style": 2,
        "phase_laterhalf_random": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100201111,
          "holder": 100201,
          "name": "最大集中",
          "startDelay": 0.4,
          "acceleration": 0.2,
          "duration": 3,
        },
        {
          "rarity": "evo",
          "id": 102602211,
          "holder": 102602,
          "name": "パーフェクトブート",
          "startDelay": 0.4,
          "passiveSpeed": 10,
          "passivePower": 10,
          "passiveStamina": 10,
          "passiveGuts": 10,
          "passiveWisdom": 10,
        },
        {
          "rarity": "evo",
          "id": 103102111,
          "holder": 103102,
          "name": "オーダーはバッチリ！",
          "startDelay": 0.4,
          "passiveSpeed": 10,
          "passivePower": 10,
          "passiveStamina": 10,
          "passiveGuts": 10,
          "passiveWisdom": 10,
        },
        {
          "rarity": "rare",
          "id": 200431,
          "name": "コンセントレーション",
          "startDelay": 0.4,
        },
        {
          "rarity": "normal",
          "id": 200432,
          "name": "集中力",
          "startDelay": 0.9,
        },
        {
          "rarity": "inherit",
          "id": 200433,
          "name": "ゲート難",
          "startDelay": 1.5,
        },
      ],
      "type": "gate",
    },
    {
      "variants": [
        { "id": 200851, "rarity": "all", "name": "逃げためらい", "speed": -0.15 },
      ],
      "duration": 3,
      "conditions": { "running_style": 1, "phase_random": 2 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -200851, "name": "逃げためらいx2", "speed": -0.3 },
      ],
      "duration": 3,
      "conditions": { "running_style": 1, "phase_random": 2 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [
        { "rarity": "all", "id": 200881, "name": "先行ためらい", "speed": -0.15 },
      ],
      "duration": 3,
      "conditions": { "running_style": 2, "phase_random": 2 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -200881, "name": "先行ためらいx2", "speed": -0.3 },
      ],
      "duration": 3,
      "conditions": { "running_style": 2, "phase_random": 2 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [
        { "rarity": "all", "id": 200911, "name": "差しためらい", "speed": -0.15 },
      ],
      "duration": 3,
      "conditions": { "running_style": 3, "phase_random": 2 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -200911, "name": "差しためらいx2", "speed": -0.3 },
      ],
      "duration": 3,
      "conditions": { "running_style": 3, "phase_random": 2 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [
        { "rarity": "all", "id": 200941, "name": "追込ためらい", "speed": -0.15 },
      ],
      "duration": 3,
      "conditions": { "running_style": 4, "phase_random": 2 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -200941, "name": "追込ためらいx2", "speed": -0.3 },
      ],
      "duration": 3,
      "conditions": { "running_style": 4, "phase_random": 2 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [{ "rarity": "all", "id": 201012, "name": "後方釘付", "speed": -0.2 }],
      "duration": 3,
      "conditions": { "distance_type": 1, "phase_random": 0 },
    },
    {
      "variants": [{ "rarity": "all", "id": 201011, "name": "悩殺術", "speed": -0.25 }],
      "duration": 3,
      "conditions": { "distance_type": 1, "phase_random": 0 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -201082, "name": "スピードイーター", "speed": -0.15 },
      ],
      "duration": 3,
      "conditions": { "distance_type": 2, "phase_random": 1 },
    },
    {
      "variants": [{ "rarity": "all", "id": 201152, "name": "束縛", "speed": -0.15 }],
      "duration": 3,
      "conditions": { "distance_type": 3, "phase_random": 2 },
    },
    {
      "variants": [{ "rarity": "all", "id": 201151, "name": "独占力", "speed": -0.25 }],
      "duration": 3,
      "conditions": { "distance_type": 3, "phase_random": 2 },
    },
    {
      "variants": [
        { "rarity": "all", "id": 201511, "name": "熱いまなざし", "speed": -0.25 },
      ],
      "duration": 3,
      "conditions": { "phase_random": 2 },
    },
    {
      "variants": [{ "rarity": "all", "id": 201512, "name": "まなざし", "speed": -0.15 }],
      "duration": 3,
      "conditions": { "phase_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "all",
          "id": -900251,
          "name": "アナタヲ・オイカケテ",
          "speed": -0.05,
        },
      ],
      "duration": 6,
      "conditions": { "distance_rate_after_random": 50 },
    },
    {
      "variants": [
        {
          "rarity": "all",
          "id": -9002512,
          "name": "アナタヲ・オイカケテ(継承)",
          "speed": -0.025,
        },
      ],
      "duration": 3.6,
      "conditions": { "distance_rate_after_random": 50 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -201222, "name": "スタミナイーター", "fatigue": 50 },
      ],
      "conditions": { "distance_type": 4, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "all",
          "id": -2012222,
          "name": "スタミナイーターx2",
          "fatigue": 100,
        },
      ],
      "conditions": { "distance_type": 4, "phase_random": 1 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [
        { "rarity": "all", "id": 200831, "name": "逃げけん制", "fatigue": 100 },
      ],
      "conditions": { "running_style": 1, "phase_random": 0, "accumulatetime": 5 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -200831, "name": "逃げけん制x2", "fatigue": 200 },
      ],
      "conditions": { "running_style": 1, "phase_random": 0, "accumulatetime": 5 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [
        { "rarity": "all", "id": 200861, "name": "先行けん制", "fatigue": 100 },
      ],
      "conditions": { "running_style": 2, "phase_random": 0, "accumulatetime": 5 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -200861, "name": "先行けん制x2", "fatigue": 200 },
      ],
      "conditions": { "running_style": 2, "phase_random": 0, "accumulatetime": 5 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [
        { "rarity": "all", "id": 200891, "name": "差しけん制", "fatigue": 100 },
      ],
      "conditions": { "running_style": 3, "phase_random": 0, "accumulatetime": 5 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -200891, "name": "差しけん制x2", "fatigue": 200 },
      ],
      "conditions": { "running_style": 3, "phase_random": 0, "accumulatetime": 5 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [
        { "rarity": "all", "id": 200921, "name": "追込けん制", "fatigue": 100 },
      ],
      "conditions": { "running_style": 4, "phase_random": 0, "accumulatetime": 5 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -200921, "name": "追込けん制x2", "fatigue": 200 },
      ],
      "conditions": { "running_style": 4, "phase_random": 0, "accumulatetime": 5 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [{ "rarity": "all", "id": 200841, "name": "逃げ焦り", "fatigue": 100 }],
      "conditions": { "running_style": 1, "phase_random": 1 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -200841, "name": "逃げ焦りx2", "fatigue": 200 },
      ],
      "conditions": { "running_style": 1, "phase_random": 1 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [{ "rarity": "all", "id": 200861, "name": "先行焦り", "fatigue": 100 }],
      "conditions": { "running_style": 2, "phase_random": 1 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -200861, "name": "先行焦りx2", "fatigue": 200 },
      ],
      "conditions": { "running_style": 2, "phase_random": 1 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [{ "rarity": "all", "id": 200901, "name": "差し焦り", "fatigue": 100 }],
      "conditions": { "running_style": 3, "phase_random": 1 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -200901, "name": "差し焦りx2", "fatigue": 200 },
      ],
      "conditions": { "running_style": 3, "phase_random": 1 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [{ "rarity": "all", "id": 200931, "name": "追込焦り", "fatigue": 100 }],
      "conditions": { "running_style": 4, "phase_random": 1 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -200931, "name": "追込焦りx2", "fatigue": 200 },
      ],
      "conditions": { "running_style": 4, "phase_random": 1 },
      "tooltip": "2回同時に喰らう。通常のと加算できる。",
    },
    {
      "variants": [
        { "rarity": "all", "id": 201021, "name": "逃亡禁止令", "fatigue": 300 },
      ],
      "conditions": { "distance_type": 1, "phase_random": 0 },
    },
    {
      "variants": [
        { "rarity": "all", "id": 201022, "name": "抜け駆け禁止", "fatigue": 100 },
      ],
      "conditions": { "distance_type": 1, "phase_random": 0 },
    },
    {
      "variants": [{ "rarity": "all", "id": 201162, "name": "ささやき", "fatigue": 100 }],
      "conditions": { "distance_type": 3, "phase_random": 1 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -201162, "name": "ささやきx2", "fatigue": 200 },
      ],
      "conditions": { "distance_type": 3, "phase_random": 1 },
    },
    {
      "variants": [
        { "rarity": "all", "id": 201161, "name": "魅惑のささやき", "fatigue": 300 },
      ],
    },
    {
      "variants": [
        { "rarity": "all", "id": -201161, "name": "魅惑のささやきx2", "fatigue": 600 },
      ],
    },
    {
      "variants": [{ "rarity": "all", "id": 201442, "name": "鋭い眼光", "fatigue": 100 }],
      "conditions": { "phase_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "all",
          "id": -201442,
          "name": "鋭い眼光x2",
          "fatigue": 200,
        },
      ],
      "conditions": { "phase_random": 2 },
    },
    {
      "variants": [
        { "rarity": "all", "id": 201441, "name": "八方にらみ", "fatigue": 300 },
      ],
      "conditions": { "phase_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "all",
          "id": -201441,
          "name": "八方にらみx2",
          "fatigue": 600,
        },
      ],
      "conditions": { "phase_random": 2 },
    },
    {
      "variants": [
        { "rarity": "all", "id": -201221, "name": "スタミナグリード", "fatigue": 100 },
      ],
      "conditions": { "distance_type": 4, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103201111,
          "holder": 103201,
          "name": "可能性の徒",
          "speedWithDecel": 0.45,
        },
        {
          "rarity": "evo",
          "id": 103702111,
          "holder": 103702,
          "name": "勝利のレシピ",
          "speedWithDecel": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 103702121,
          "holder": 103702,
          "name": "仕込みは完璧",
          "heal": 750,
          "conditions": {
            "distance_type": 3,
            "phase_random": 1,
          },
        },
        {
          "rarity": "evo",
          "id": 101701111,
          "holder": 101701,
          "name": "皇帝の眼差し",
          "speedWithDecel": 0.45,
        },
        {
          "rarity": "evo",
          "id": 106002111,
          "holder": 106002,
          "name": "頑張るしかないよね",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "evo",
          "id": 101101111,
          "holder": 101101,
          "name": "大和撫子",
          "targetSpeed": 0.45,
        },
      ],
      "duration": 3,
      "conditions": {
        "distance_type": 3,
        "phase_random": 2,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104501111,
          "holder": 104501,
          "name": "あま～い幻惑",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "evo",
          "id": 101801111,
          "holder": 101801,
          "name": "女帝の権謀",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "evo",
          "id": 103801111,
          "holder": 103801,
          "name": "#カワイイかく乱♪",
          "speedWithDecel": 0.15,
          "duration": 3,
        },
      ],
      "duration": 4,
      "conditions": { "running_style": 2, "phase_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106001211,
          "holder": 106001,
          "name": "目指せ！主人公！",
          "targetSpeed": 0.35,
        },
      ],
      "duration": 4,
      "conditions": { "running_style": 3, "phase_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104901111,
          "holder": 104901,
          "name": "乾坤一擲の大博打",
          "targetSpeed": 0.55,
        },
        {
          "rarity": "rare",
          "id": 202031,
          "name": "博打うち",
          "targetSpeed": 0.45,
        },
        {
          "rarity": "normal",
          "id": 202032,
          "name": "あやしげな作戦",
          "targetSpeed": 0.25,
        },
      ],
      "type": "speed",
      "duration": 1.8,
      "conditions": { "distance_rate_after_random": 50 },
      "trigger": "function () {
        const dice = Math.random();
        if (dice < 0.1) {
          thiz.sp -= 0.04 * thiz.spMax;
        } else if (dice < 0.4) {
          thiz.sp -= 0.02 * thiz.spMax;
        }
      }",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105701111,
          "holder": 105701,
          "name": "天翔る足取り",
          "speedWithDecel": 0.45,
        },
        {
          "rarity": "rare",
          "id": 202521,
          "name": "天衣無縫",
          "speedWithDecel": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202522,
          "name": "型破り",
          "speedWithDecel": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": [3, 4], "corner_random": [3] },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102801111,
          "holder": 102801,
          "name": "下ごしらえ万全！",
          "acceleration": 0.3,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "rare",
          "id": 201001,
          "name": "準備万全！",
          "acceleration": 0.3,
        },
        {
          "rarity": "normal",
          "id": 201002,
          "name": "仕掛け準備",
          "acceleration": 0.2,
        },
      ],
      "duration": 4,
      "conditions": { "distance_type": 1, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105601111,
          "holder": 105601,
          "name": "吉兆です！",
          "targetSpeed": 0.35,
        },
      ],
      "duration": 4,
      "conditions": { "distance_type": 4, "phase_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106001111,
          "holder": 106001,
          "name": "魅惑のシニカルガール",
          "acceleration": 0.4,
        },
      ],
      "duration": 4,
      "conditions": { "distance_type": 3, "phase_random": 2 },
      "tooltip":
        "終盤前半ランダム位置として扱うが、要するに昔のノンストなのでゴミだよこれ",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102402111,
          "holder": 102402,
          "name": "純白のささやき",
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 104502211,
          "holder": 104502,
          "name": "魅惑のマミーウィスパー",
          "heal": 150,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": 3, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 102501211,
          "holder": 102501,
          "name": "オイテイカナイデ",
          "heal": 350,
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 106702211,
          "holder": 106702,
          "name": "明けぬ夜はない！",
          "heal": 350,
          "targetSpeed": 0.15,
        },
      ],
      "type": "heal",
      "duration": 3,
      "conditions": { "distance_type": 4, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103701111,
          "holder": 103701,
          "name": "閃光のマギア",
          "speedWithDecel": 0.25,
        },
        {
          "rarity": "evo",
          "id": 101801211,
          "holder": 101801,
          "name": "水月鏡花",
          "speedWithDecel": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "phase_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100701111,
          "holder": 100701,
          "name": "564アイズで視界クッキリ！",
          "heal": 150,
        },
      ],
      "conditions": { "phase_random": 0 },
      "tooltip": "序盤のどこかで発動として扱う",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 101401111,
          "holder": 101401,
          "name": "鷹ノ目",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": 3, "phase_random": 0 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 106101211,
          "holder": 106101,
          "name": "王の大局観",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "running_style": 3, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103801211,
          "holder": 103801,
          "name": "#夢中になっちゃえ♪",
          "targetSpeed": 0.15,
        },
        {
          "rarity": "evo",
          "id": 103802111,
          "holder": 103802,
          "name": "#カレンに染まってみる？",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 3,
      "conditions": {
        "distance_type": 1,
        "phase_random": 0,
        "accumulatetime": 5,
        "running_style": [1, 2],
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104401211,
          "holder": 104401,
          "name": "チャームマジック",
          "speedWithDecel": 0.15,
        },
        {
          "rarity": "evo",
          "id": 104401221,
          "holder": 104401,
          "name": "サルビア☆スプレンデンス",
          "speedWithDecel": 0.35,
        },
      ],
      "duration": 3,
      "conditions": { "running_style": 4, "phase_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 105901111,
          "holder": 105901,
          "name": "クールな視線",
          "speedWithDecel": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "running_style": 3, "phase_random": 2 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104002111,
          "holder": 104002,
          "name": "鮮やかな布陣",
          "heal": 150,
        },
      ],
      "conditions": {
        "distance_type": 2,
        "phase_random": 0,
        "accumulatetime": 3,
        "running_style": [3, 4],
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100502111,
          "holder": 100502,
          "name": "圧巻のトリック",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "evo",
          "id": 103002211,
          "holder": 103002,
          "name": "トリック&トリート",
          "targetSpeed": 0.35,
        },
      ],
      "duration": 3,
      "conditions": {
        "phase_random": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 108601211,
          "holder": 108601,
          "name": "高嶺の艶笑",
          "targetSpeed": 0.35,
          "duration": 4,
        },
        {
          "rarity": "rare",
          "id": 202531,
          "name": "優位形成",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202532,
          "name": "しとやかな足取り",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": { "distance_type": [2, 3], "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 108501111,
          "holder": 108501,
          "name": "華麗であれ",
          "targetSpeed": 0.45,
          "tooltip": "デバフは前後各３人で、-0.15。",
        },
        {
          "rarity": "rare",
          "id": 202541,
          "name": "威風堂々",
          "targetSpeed": 0.35,
          "tooltip": "デバフは前後各３人で、-0.15。",
        },
        {
          "rarity": "normal",
          "id": 202542,
          "name": "プレッシャー",
          "targetSpeed": 0.15,
          "tooltip": "デバフは前後各３人で、-0.035。",
        },
      ],
      "duration": 1.8,
      "conditions": { "running_style": 3, "is_finalcorner_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 104601111,
          "holder": 104601,
          "name": "大注目のウマドル参上★",
          "tooltip": "エミュでは意味無し",
        },
        {
          "rarity": "evo",
          "id": 104601121,
          "holder": 104601,
          "name": "目を逸らしちゃダメだよ☆",
          "speedWithDecel": 0.25,
          "duration": 4,
          "conditions": { "phase_random": 2 },
        },
        {
          "rarity": "evo",
          "id": 105202111,
          "holder": 105202,
          "name": "見てて見てて！",
          "targetSpeed": 0.15,
          "duration": 3,
        },
      ],
      "conditions": { "phase_random": 0 },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 202551,
          "name": "神速",
          "speedWithDecel": 0.35,
          "heal": 150,
        },
        {
          "rarity": "normal",
          "id": 202552,
          "name": "快速",
          "speedWithDecel": 0.15,
          "heal": 35,
        },
      ],
      "duration": 3,
      "conditions": { "distance_rate_after_random": 50 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 108301211,
          "holder": 108301,
          "name": "革命をもたらす威容",
          "targetSpeed": 0.35,
          "heal": 150,
        },
        {
          "rarity": "rare",
          "id": 202561,
          "name": "一発必中",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202562,
          "name": "ロックオン",
          "targetSpeed": 0.15,
        },
      ],
      "type": "speed",
      "duration": 2.4,
      "conditions": { "distance_type": 4, "phase_laterhalf_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 210261,
          "name": "太陽の叡智",
          "targetSpeed": 0.4025,
        },
        {
          "rarity": "normal",
          "id": 210262,
          "name": "陽の加護",
          "targetSpeed": 0.1725,
        },
      ],
      "duration": 1.8,
      "conditions": { "phase_random": 1 },
      "tooltip": "1.15倍(スキル15個)、中盤ランダムとして扱う",
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 210281,
          "name": "大地の叡智",
          "targetSpeed": 0.4025,
        },
        {
          "rarity": "normal",
          "id": 210282,
          "name": "地の加護",
          "targetSpeed": 0.1725,
        },
      ],
      "duration": 2.4,
      "conditions": { "phase_laterhalf_random": 2 },
      "tooltip": "1.15倍(スキル15個)として扱う",
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 210271,
          "name": "大海の叡智",
          "acceleration": 0.46,
        },
        {
          "rarity": "normal",
          "id": 210272,
          "name": "海の加護",
          "acceleration": 0.23,
        },
      ],
      "duration": 1.5,
      "conditions": { "phase_firsthalf_random": 0 },
      "tooltip": "1.15倍(スキル15個)として扱う",
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 202581,
          "name": "脱兎の先へ",
          "targetSpeed": 0.25,
          "heal": 550,
        },
        {
          "rarity": "normal",
          "id": 202582,
          "name": "脇目も振らず",
          "targetSpeed": 0.05,
          "heal": 150,
        },
      ],
      "duration": 2.4,
      "conditions": { "distance_type": 4, "phase_random": 1 },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "holder": 107601,
          "id": 107601211,
          "name": "一花咲かせましょう！",
          "speedWithDecel": 0.45,
        },
        {
          "rarity": "rare",
          "id": 202591,
          "name": "一気呵成",
          "speedWithDecel": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202592,
          "name": "大急ぎ",
          "speedWithDecel": 0.15,
        },
      ],
      "duration": 3,
      "conditions": {
        "distance_type": 4,
        "running_style": 3,
        "phase_firsthalf_random": 2,
        "lastspurt": 2,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "holder": 110501,
          "id": 107601211,
          "name": "星の海を駆けて",
          "speedWithDecel": 0.35,
          "duration": 4,
        },
        {
          "rarity": "rare",
          "id": 202601,
          "name": "ルミネセンス",
          "speedWithDecel": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202602,
          "name": "イグニッション",
          "speedWithDecel": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": {
        "distance_type": 3,
        "activate_count_all": 13,
      },
    },
    {
      "variants": [
        {
          "rarity": "normal",
          "id": 202612,
          "name": "溢れる情熱",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": {
        "running_style": 3,
        "phase_laterhalf_random": 0,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 110601211,
          "holder": 110601,
          "name": "ちょっといいこと、集めて",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202641,
          "name": "千里の道",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 202642,
          "name": "一歩から",
          "targetSpeed": 0.05,
        },
      ],
      "duration": 4,
      "conditions": {
        "running_style": 3,
        "phase_random": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 202661,
          "name": "昂る鼓動",
          "speedWithDecel": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202662,
          "name": "込み上げる熱",
          "speedWithDecel": 0.15,
        },
      ],
      "duration": 3,
      "conditions": {
        "distance_type": [3, 4],
        "phase_firsthalf_random": 2,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 108401211,
          "holder": 108401,
          "name": "酔い痴れよ、世界",
          "passiveSpeed": 80,
          "passivePower": 80,
        },
        {
          "rarity": "rare",
          "id": 202651,
          "name": "雲蒸竜変",
          "passiveSpeed": 60,
          "passivePower": 60,
        },
        {
          "rarity": "normal",
          "id": 202652,
          "name": "自信家",
          "passiveSpeed": 20,
          "passivePower": 20,
        },
      ],
      "conditions": {
        "distance_type": [2, 3],
        "base_speed": 1000,
        "base_power": 1000,
      },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 202681,
          "name": "ブチ☆アゲ↑バイブス",
          "targetSpeed": 0.35,
          "heal": 350,
        },
        {
          "rarity": "normal",
          "id": 202682,
          "name": "バイブス上昇",
          "targetSpeed": 0.15,
          "heal": 50,
        },
      ],
      "duration": 2.4,
      "conditions": {
        "distance_type": 4,
        "phase_random": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 202671,
          "name": "ハイボルテージ",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 202672,
          "name": "心弾んで",
          "acceleration": 0.2,
        },
      ],
      "duration": 1.8,
      "conditions": {
        "distance_type": 2,
        "phase_firsthalf_random": 2,
      },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 202621,
          "name": "突撃魂",
          "targetSpeed": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202622,
          "name": "進出開始",
          "targetSpeed": 0.15,
        },
      ],
      "duration": 2.4,
      "conditions": {
        "running_style": 3,
        "phase_laterhalf_random": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 202711,
          "name": "王手",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 202712,
          "name": "会心の一歩",
          "acceleration": 0.2,
        },
      ],
      "duration": 0.9,
      "conditions": {
        "running_style": [2, 3],
        "phase": ">=2",
        "is_finalcorner": 1,
        "corner": 1,
        "remain_distance": ">=600",
      },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 202721,
          "name": "ネバーギブアップ",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 202722,
          "name": "折れない心",
          "targetSpeed": 0.05,
        },
      ],
      "duration": 4,
      "conditions": {
        "phase_corner_random": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 210291,
          "name": "最高峰の夢",
          "targetSpeed": 0.42,
        },
        {
          "rarity": "normal",
          "id": 210292,
          "name": "想いを背負って",
          "targetSpeed": 0.18,
        },
      ],
      "duration": 2.4,
      "tooltip": "1.2倍として扱う",
      "conditions": {
        "is_last_straight": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 103202111,
          "holder": 103202,
          "name": "ラプラスの悪魔",
        },
      ],
      "type": "passive",
      "conditions": { "running_style": 2, "distance_type": 3 },
      "trigger": "function () {
        if (thiz.umaStatus.speed >= 1200 && thiz.umaStatus.wisdom >= 1200) {
          thiz.passiveBonus.speed += 100;
        } else if (thiz.umaStatus.speed >= 1200) {
          thiz.passiveBonus.speed += 80;
        }
      }",
    },
    {
      "variants": [{ "rarity": "rare", "id": 202701, "name": "シンギュラリティ" }],
      "type": "passive",
      "conditions": { "running_style": 2, "distance_type": 3 },
      "trigger": "function () {
        if (thiz.umaStatus.speed >= 1200 && thiz.umaStatus.wisdom >= 1200) {
          thiz.passiveBonus.speed += 80;
        } else if (thiz.umaStatus.speed >= 1200) {
          thiz.passiveBonus.speed += 60;
        }
      }",
    },
    {
      "variants": [{ "rarity": "normal", "id": 202702, "name": "探求心" }],
      "type": "passive",
      "conditions": { "running_style": 2, "distance_type": 3 },
      "trigger": "function () {
        if (thiz.umaStatus.speed >= 1200 && thiz.umaStatus.wisdom >= 1200) {
          thiz.passiveBonus.speed += 40;
        } else if (thiz.umaStatus.speed >= 1200) {
          thiz.passiveBonus.speed += 20;
        }
      }",
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 100202211,
          "holder": 100202,
          "name": "蒼天を駆けるラーファガ",
          "acceleration": 0.5,
        },
        {
          "rarity": "rare",
          "id": 202691,
          "name": "誰より前へ！",
          "acceleration": 0.4,
        },
        {
          "rarity": "normal",
          "id": 202692,
          "name": "一番乗り",
          "acceleration": 0.2,
        },
      ],
      "duration": 1.5,
      "conditions": {
        "distance_type": 3,
        "phase": 0,
      },
    },
    {
      "variants": [
        {
          "rarity": "evo",
          "id": 108601111,
          "holder": 108601,
          "name": "純愛",
          "speedWithDecel": 0.45,
        },
        {
          "rarity": "rare",
          "id": 202741,
          "name": "十全十美",
          "speedWithDecel": 0.35,
        },
        {
          "rarity": "normal",
          "id": 202742,
          "name": "品行方正",
          "speedWithDecel": 0.15,
        },
      ],
      "duration": 3,
      "conditions": { "distance_type": [2, 3], "corner_random": 3 },
    },
    {
      "variants": [
        {
          "rarity": "rare",
          "id": 202761,
          "name": "かっとばせ！",
          "targetSpeed": 0.25,
        },
        {
          "rarity": "normal",
          "id": 202762,
          "name": "勝利に向かって",
          "targetSpeed": 0.05,
        },
      ],
      "duration": 4,
      "conditions": {
        "running_style": 2,
        "phase_random": 1,
      },
    },
    {
      "variants": [
        {
          "rarity": "normal",
          "id": 202772,
          "name": "狙いを定めて",
          "acceleration": 0.2,
        },
      ],
      "duration": 1.2,
      "conditions": {
        "running_style": 2,
        "distance_type": 4,
        "phase_firstquarter_random": 2,
      },
    },
  ]
"""

internal val rawUniqueSkillData = """
[
  {
    "id": 0,
    "name": "  なし／発動しない",
    "noInherit": true,
    "check": "function () {
      return false;
    }",
  },
  {
    "id": 100321,
    "holder": 103201,
    "name": "U=ma2",
    "heal": 550,
    "duration": 4,
    "targetSpeed": 0.25,
    "tooltip": "3～4位(<=40%)",
    "check": "function () {
      return (
        thiz.position >= thiz.courseLength / 2.0 &&
        thiz.isInCorner(thiz.position)
      );
    }",
  },
  {
    "id": 100451,
    "holder": 104501,
    "name": "ピュリティオブハート",
    "heal": 750,
    "tooltip": "2～4位(<=40%)",
    "init": "function () {
      thiz.randoms = thiz.initPhaseRandom(1);
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100521,
    "holder": 105201,
    "name": "ワクワククライマックス",
    "heal": 550,
    "tooltip": "近くにウマ娘がいる＆順位<=50%は満たしていると見なす",
    "check": "function () {
      return thiz.isInFinalCorner();
    }",
  },
  {
    "id": 110111,
    "holder": 101102,
    "name": "ゲインヒール・スペリアー",
    "heal": 750,
    "tooltip": "中盤のどこかで発動として見なす。",
    "init": "function () {
      thiz.randoms = thiz.initPhaseRandom(1);
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 110011,
    "holder": 100102,
    "name": "わやかわ♪マリンダイヴ",
    "heal": 550,
    "check": "function () {
      return thiz.skillTriggerCount[1] >= 2;
    }",
  },
  {
    "id": 110301,
    "holder": 103002,
    "name": "Drain for rose",
    "heal": 550,
    "targetSpeed": 0.25,
    "duration": 5,
    "conditions": {
      "phase": 1,
    },
  },
  {
    "id": 100071,
    "holder": 100701,
    "name": "不沈艦、抜錨ォッ！",
    "targetSpeed": 0.25,
    "duration": 6,
    "tooltip": "順位条件の<=50%は満たしていると見なす",
    "check": "function () {
      return (
        thiz.position >= thiz.courseLength * 0.5 &&
        thiz.position <= thiz.courseLength * 0.6
      );
    }",
  },
  {
    "id": 100131,
    "holder": 101301,
    "name": "貴顕の使命を果たすべく",
    "duration": 5,
    "type": "speed",
    "invokes": [
      {
        "targetSpeed": 0.45,
        "conditions": {
          "is_finalcorner": 1,
          "corner": 1,
          "distance_type": 4,
          "lastspurt": 2,
        },
      },
      {
        "targetSpeed": 0.35,
        "conditions": {
          "is_finalcorner": 1,
          "corner": 1,
        },
      },
    ],
  },
  {
    "id": 100261,
    "holder": 102601,
    "name": "G00 1st．F∞;",
    "targetSpeed": 0.45,
    "duration": 5,
    "conditions": {
      "is_badstart": 0,
      "is_finalcorner": 1,
      "corner": 0,
    },
  },
  {
    "id": 110131,
    "holder": 101302,
    "name": "最強の名を懸けて",
    "targetSpeed": 0.35,
    "duration": 6,
    "conditions": {
      "phase": ">=2",
      "is_finalcorner": 1,
    },
    "tooltip": "「最終直線のどこか」として扱う。",
    "init": "function () {
      thiz.randoms = thiz.initFinalStraightRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100081,
    "holder": 100801,
    "name": "カッティング×DRIVE！",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip":
      "「他面倒くさいの全部満たしたと見なして200mで発動する」として扱う。",
    "check": "function (startPosition) {
      return (
        startPosition <= thiz.toPosition(200) &&
        thiz.position >= thiz.toPosition(200)
      );
    }",
  },
  {
    "id": 100061,
    "holder": 100601,
    "name": "勝利の鼓動",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "順位条件は満たしてると見なす",
    "check": "function (startPosition) {
      return (
        startPosition <= thiz.toPosition(200) &&
        thiz.position >= thiz.toPosition(200)
      );
    }",
  },
  {
    "id": 100171,
    "holder": 101701,
    "name": "汝、皇帝の神威を見よ",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "最終コーナーで3人追い抜きは満たしたと見なす",
    "check": "function () {
      return thiz.isInFinalStraight();
    }",
  },
  {
    "id": 100181,
    "holder": 101801,
    "name": "ブレイズ・オブ・プライド",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip":
      "他面倒くさいの全部満たしたと見なし「最終コーナーのどこかで発動する」として扱う。",
    "init": "function () {
      thiz.randoms = thiz.initFinalCornerRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100351,
    "holder": 103501,
    "name": "勝利のチケットを、君にッ！",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip":
      "他面倒くさいの全部満たしたと見なし「最終直線のどこかで発動する」として扱う。",
    "init": "function () {
      thiz.randoms = thiz.initFinalStraightRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100411,
    "holder": 104101,
    "name": "優等生×バクシン＝大勝利ッ",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "「レース1/2～5/6のどこかで発動する」として扱う。",
    "init": "function () {
      thiz.randoms = thiz.initIntervalRandom(0.5, 5.0 / 6);
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100561,
    "holder": 105601,
    "name": "来ます来てます来させます！",
    "targetSpeed": 0.35,
    "acceleration": 0.1,
    "duration": 5,
    "tooltip": "「終盤のどこかで発動する」として扱う。",
    "init": "function () {
      thiz.randoms = thiz.initPhaseRandom(2);
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100601,
    "holder": 106001,
    "name": "きっとその先へ…！",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "「ラストスパートのどこかで発動する」として扱う。",
    "init": "function () {
      thiz.randoms = thiz.initPhaseRandom(3);
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100111,
    "holder": 101101,
    "name": "精神一到何事か成らざらん",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "「最終直線のどこかで発動する」として扱う。",
    "init": "function () {
      thiz.randoms = thiz.initFinalStraightRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100611,
    "holder": 106101,
    "name": "Pride of KING",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "4～6位(<=70%)",
    "check": "function (startPosition) {
      return (
        startPosition <= thiz.toPosition(200) &&
        thiz.position >= thiz.toPosition(200) &&
        thiz.temptationModeStart == null &&
        thiz.startDelay < 0.08
      );
    }",
  },
  {
    "id": 100011,
    "holder": 100101,
    "name": "シューティングスター",
    "speedWithDecel": 0.35,
    "duration": 5,
    "acceleration": 0.1,
    "tooltip": "終盤前半ランダム位置発動として扱う",
    "conditions": {
      "phase_random": 2,
    },
  },
  {
    "id": 100021,
    "holder": 100201,
    "name": "先頭の景色は譲らない…！",
    "targetSpeed": 0.35,
    "duration": 5,
    "check": "function () {
      return thiz.isInInterval(0.5, 1);
    }",
  },
  {
    "id": 100031,
    "holder": 100301,
    "name": "究極テイオーステップ",
    "targetSpeed": 0.45,
    "duration": 5,
    "conditions": {
      "is_finalcorner": 1,
      "corner": 0,
    },
  },
  {
    "id": 100151,
    "holder": 101501,
    "name": "ヴィットーリアに捧ぐ舞踏",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "「最終コーナーのどこかで発動」として扱う。まぁ発動しないけど。",
    "init": "function () {
      thiz.randoms = thiz.initFinalCornerRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100161,
    "holder": 101601,
    "name": "Shadow Break",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "競合あり、2～7位(<=75%)",
    "init": "function () {
      thiz.randoms = thiz.initFinalCornerRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100231,
    "holder": 102301,
    "name": "∴win Q．E．D．",
    "duration": 5,
    "type": "speed",
    "invokes": [
      {
        "targetSpeed": 0.45,
        "conditions": {
          "phase": ">=2",
          "is_finalcorner": 1,
          "temptation_count": 0,
        },
      },
      {
        "targetSpeed": 0.35,
        "conditions": {
          "phase": ">=2",
          "is_finalcorner": 1,
        },
      },
    ],
  },
  {
    "id": 100301,
    "holder": 103001,
    "name": "ブルーローズチェイサー",
    "targetSpeed": 0.35,
    "duration": 5,
    "conditions": {
      "phase": ">=2",
      "is_finalcorner": 1,
      "corner": 0,
    },
  },
  {
    "id": 100501,
    "holder": 105001,
    "name": "Nemesis",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "「最終コーナーのどこかで発動」として扱う",
    "init": "function () {
      thiz.randoms = thiz.initFinalCornerRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 110031,
    "holder": 100302,
    "name": "絶対は、ボクだ",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "「最終直線のどこかで発動」として扱う",
    "init": "function () {
      thiz.randoms = thiz.initFinalStraightRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 11024101,
    "holder": 102402,
    "name": "フラワリー☆マニューバ(前)",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "「最終コーナーのどこかで発動」として扱う。こちらは前の方。",
    "init": "function () {
      thiz.randoms = thiz.initFinalCornerRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 110181,
    "holder": 101802,
    "name": "薫風、永遠なる瞬間を",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "「中盤のどこかで発動」として扱うが、基本的には発動しない。",
    "init": "function () {
      thiz.randoms = thiz.initPhaseRandom(1);
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100121,
    "holder": 101201,
    "name": "タイマン！デッドヒート！",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "「最終直線のどこかで発動」として扱う。",
    "init": "function () {
      thiz.randoms = thiz.initFinalStraightRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 110041,
    "holder": 100402,
    "name": "グッときて♪Chu",
    "targetSpeed": 0.35,
    "duration": 5,
    "check": "function () {
      return (
        thiz.position >= thiz.courseLength * 0.5 && thiz.healTriggerCount > 0
      );
    }",
  },
  {
    "id": 100371,
    "holder": 103701,
    "name": "Schwarzes Schwert",
    "targetSpeed": 0.45,
    "duration": 5,
    "conditions": {
      "temptation_count": 0,
      "is_finalcorner": 1,
      "corner": 0,
    },
  },
  {
    "id": 110561,
    "holder": 105602,
    "name": "禾スナハチ登ル",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "50%-60%地点のどこかで発動として扱う",
    "init": "function () {
      thiz.randoms = thiz.initIntervalRandom(0.5, 0.6);
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100191,
    "holder": 101901,
    "name": "尊み☆ﾗｽﾄｽﾊﾟ━━(ﾟ∀ﾟ)━━ﾄ!",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "フェイズ2のどこか発動として扱う",
    "init": "function () {
      thiz.randoms = thiz.initPhaseRandom(2);
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100391,
    "holder": 103901,
    "name": "姫たるもの、勝利をこの手に",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "最終直線に入ってすぐ発動として扱う",
    "check": "function () {
      return thiz.isInFinalStraight();
    }",
  },
  {
    "id": 100251,
    "holder": 102501,
    "name": "アナタヲ・オイカケテ",
    "targetSpeed": 0.25,
    "duration": 6,
    "tooltip": "順位条件は満たしていると見なす",
    "check": "function () {
      return thiz.position >= thiz.courseLength * 0.5;
    }",
  },
  {
    "id": 110171,
    "holder": 101702,
    "name": "翳り退く、さざめきの矢",
    "targetSpeed": 0.35,
    "duration": 6,
    "conditions": {
      "phase": ">=2",
      "is_finalcorner": 1,
    },
  },
  {
    "id": 100481,
    "holder": 104801,
    "name": "YEAH☆VIVID TIME!",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "最終直線即発動として扱う",
    "conditions": {
      "is_finalcorner": 1,
      "corner": 0,
    },
  },
  {
    "id": 110231,
    "holder": 102302,
    "name": "Presents from X",
    "targetSpeed": 0.35,
    "duration": 5,
    "conditions": {
      "phase": 1,
      "distance_rate": ">=50",
    },
  },
  {
    "id": 100041,
    "holder": 100401,
    "name": "紅焔ギア/LP1211-M",
    "acceleration": 0.4,
    "duration": 4,
    "tooltip":
      "順位<=5及び<=50%は満たしていると見なす。どの脚質でも最速発動扱い。",
    "check": "function () {
      return thiz.isInFinalCorner() || thiz.isInFinalStraight();
    }",
  },
  {
    "id": 100101,
    "holder": 101001,
    "name": "ヴィクトリーショット！",
    "acceleration": 0.4,
    "duration": 5,
    "tooltip": "順位>=3及び<=50%は満たしていると見なす",
    "check": "function () {
      return thiz.isInFinalCorner(thiz.position, { \"start\": 0.5, \"end\": 1 });
    }",
  },
  {
    "id": 100271,
    "holder": 102701,
    "name": "レッツ・アナボリック！",
    "acceleration": 0.4,
    "duration": 4,
    "conditions": { "phase": ">=2", "corner": 1, "running_style": [3, 4] },
  },
  {
    "id": 100201,
    "holder": 102001,
    "name": "アングリング×スキーミング",
    "acceleration": 0.4,
    "duration": 4,
    "conditions": { "phase": ">=2", "corner": 1, "running_style": [1, 2] },
  },
  {
    "id": 11024102,
    "holder": 102402,
    "name": "フラワリー☆マニューバ(後)",
    "acceleration": 0.4,
    "duration": 4,
    "tooltip": "「最終コーナーのどこかで発動」として扱う。こちらは後の方。",
    "init": "function () {
      thiz.randoms = thiz.initFinalCornerRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 110141,
    "holder": 101402,
    "name": "コンドル猛撃波",
    "acceleration": 0.4,
    "duration": 4,
    "init": "function () {
      thiz.randoms = thiz.initFinalCornerRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 100401,
    "holder": 104001,
    "name": "KEEP IT REAL．",
    "acceleration": 0.3,
    "duration": 6,
    "tooltip": "50%地点で即発動として扱う",
    "check": "function () {
      return thiz.position >= thiz.courseLength / 2.0;
    }",
  },
  {
    "id": 100591,
    "holder": 105901,
    "name": "彼方、その先へ…",
    "acceleration": 0.4,
    "duration": 4,
    "tooltip": "常に順位>=50%及び<=70%は満たしていると見なす。",
    "check": "function (startPosition) {
      return (
        thiz.temptationModeStart == null &&
        ((thiz.currentPhase >= 2 &&
          !thiz.isInFinalCorner(startPosition) &&
          thiz.isInCorner(startPosition)) ||
          (thiz.currentPhase === 1 && thiz.isInFinalCorner(startPosition)))
      );
    }",
  },
  {
    "id": 100091,
    "holder": 100901,
    "name": "ブリリアント・レッドエース",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "duration": 5,
    "tooltip": "「レース50%～75%のどこかで発動する」として扱う。",
    "conditions": {
      "distance_rate_random": [50, 75],
    },
  },
  {
    "id": 100141,
    "holder": 101401,
    "name": "プランチャ☆ガナドール",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "duration": 5,
    "tooltip": "最終直線即発動として扱う。",
    "conditions": {
      "is_finalcorner": 1,
      "corner": 0,
      "hp_per": ">=30",
    },
  },
  {
    "id": 100241,
    "holder": 102401,
    "name": "ひらめき☆ランディング",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "duration": 5,
    "tooltip": "最終コーナーで即発動として扱う",
    "conditions": {
      "is_finalcorner": 1,
    },
  },
  {
    "id": 100381,
    "holder": 103801,
    "name": "#LookatCurren",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "duration": 5,
    "tooltip": "2～4位(<=40%)。レース50%-65%のどこかで発動。",
    "conditions": {
      "distance_rate_random": [50, 65],
    },
  },
  {
    "id": 100461,
    "holder": 104601,
    "name": "キラキラ☆STARDOM",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "duration": 5,
    "tooltip": "中盤のコーナーではない地点と即発動としてみなす",
    "conditions": {
      "phase": 1,
      "corner": 0,
    },
  },
  {
    "id": 100581,
    "holder": 105801,
    "name": "I Never Goof Up!",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "duration": 5,
    "conditions": {
      "phase_random": 2,
    },
    "tooltip": "「終盤前半ランダムで発動」として扱う",
  },
  {
    "id": 100281,
    "holder": 102801,
    "name": "I’M☆FULL☆SPEED!!",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "duration": 5,
    "conditions": {
      "distance_rate": [45, 60],
      "hp_per": "<=70",
    },
  },
  {
    "id": 110451,
    "holder": 104502,
    "name": "ぐるぐるマミートリック♡",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "duration": 5,
    "tooltip": "最終直線に入った瞬間に発動として扱う",
    "conditions": {
      "is_finalcorner": 1,
      "corner": 0,
    },
  },
  {
    "id": 110401,
    "holder": 104002,
    "name": "GET DOWN",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "duration": 5,
    "tooltip": "最終コーナーに入った瞬間に発動として扱う",
    "conditions": {
      "is_finalcorner": 1,
      "corner": 1,
    },
  },
  {
    "id": 110061,
    "holder": 100602,
    "name": "聖夜のミラクルラン！",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "heal": 350,
    "duration": 5,
    "check": "function () {
      return (
        thiz.position >= thiz.courseLength * 0.5 && thiz.healTriggerCount >= 3
      );
    }",
  },
  {
    "id": 100221,
    "holder": 102201,
    "name": "Fairy tale",
    "targetSpeed": 0.35,
    "duration": 5,
    "check": "function () {
      return thiz.position >= thiz.courseLength * 0.52;
    }",
  },
  {
    "id": 100211,
    "holder": 102101,
    "name": "白い稲妻、見せたるで！",
    "targetSpeed": 0.35,
    "acceleration": 0.1,
    "duration": 5,
    "check": "function () {
      return thiz.isInStraight() && thiz.position >= thiz.courseLength * 0.5;
    }",
  },
  {
    "id": 110521,
    "holder": 105202,
    "name": "113転び114起き",
    "targetSpeed": 0.25,
    "duration": 6,
    "tooltip": "6秒持続として扱う",
    "check": "function () {
      return thiz.isInFinalCorner();
    }",
  },
  {
    "id": 110151,
    "holder": 101502,
    "name": "恵福バルカローレ",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "7回発動したとして扱う。じゃないと弱すぎる。",
    "check": "function (startPosition) {
      return (
        startPosition <= thiz.toPosition(400) &&
        thiz.position >= thiz.toPosition(400)
      );
    }",
  },
  {
    "id": 100691,
    "holder": 106901,
    "name": "憧れは桜を越える！",
    "targetSpeed": 0.35,
    "duration": 5,
    "check": "function (startPosition) {
      return (
        startPosition <= thiz.toPosition(300) &&
        thiz.position >= thiz.toPosition(300)
      );
    }",
  },
  {
    "id": 110261,
    "holder": 102602,
    "name": "オペレーション・Cacao",
    "targetSpeed": 0.35,
    "heal": 150,
    "duration": 5,
    "check": "function () {
      return thiz.currentPhase === 1 && thiz.isInCorner();
    }",
  },
  {
    "id": 110371,
    "holder": 103702,
    "name": "Guten Appetit♪",
    "targetSpeed": 0.35,
    "duration": 6,
    "tooltip": "最終コーナー以降で2人追い抜きは満たしたと見なす",
    "check": "function () {
      return thiz.isInFinalStraight();
    }",
  },
  {
    "id": 100331,
    "holder": 103301,
    "name": "ディオスクロイの流星",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip":
      "7～9位(>=80%)の場合。他の場合は他の最終直線0.35固有で代用して下さい。",
    "check": "function () {
      return thiz.isInFinalStraight();
    }",
  },
  {
    "id": 100681,
    "holder": 106801,
    "name": "勝ち鬨ワッショイ！",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "duration": 5,
    "tooltip": "1～2位",
    "check": "function () {
      return (
        (thiz.isPhase(2) && thiz.isStraightFrontType(2)) ||
        (thiz.isInInterval(0.5, 1) && thiz.isInCorner(null, 3))
      );
    }",
  },
  {
    "id": 100621,
    "holder": 106201,
    "name": "どんっ、パッ、むんっ",
    "targetSpeed": 0.25,
    "heal": 550,
    "duration": 5,
    "tooltip": "3～6位(<=70%)",
    "check": "function () {
      return thiz.isInInterval(0.5, 1) && thiz.isPhase(1);
    }",
  },
  {
    "id": 100051,
    "holder": 100501,
    "name": "煌星のヴォードヴィル",
    "targetSpeed": 0.45,
    "duration": 5,
    "conditions": {
      "remain_distance": "<=300",
    },
  },
  {
    "id": 100671,
    "holder": 106701,
    "name": "晦冥を照らせ永遠の耀き",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "2～5位。発動時先頭から5m以内の場合。",
    "check": "function () {
      return thiz.isInFinalStraight();
    }",
  },
  {
    "id": 100711,
    "holder": 107101,
    "name": "一期の夢、刹那の飛翔",
    "targetSpeed": 0.45,
    "heal": -100,
    "duration": 4,
    "check": "function () {
      return thiz.isInFinalStraight();
    }",
  },
  {
    "id": 100741,
    "holder": 107401,
    "name": "麗しき花信風",
    "targetSpeed": 0.15,
    "duration": 5,
    "tooltip": "4～7位",
    "conditions": {
      "distance_rate": ">=50",
    },
    "trigger": "function (skill) {
      const map = {
        \"2000\": 1,
        \"2400\": 1.5,
        \"2600\": 2,
        \"2800\": 2.2,
        \"3000\": 2.5,
        \"3200\": 3,
        \"3500\": 3.5,
        \"99999999\": 4,
      };
      for (const key in map) {
        if (thiz.sp < key) {
          skill.durationOverwrite = skill.duration * map[key];
          return {
            \"extended\": map[key].toString(),
          };
        }
      }
    }",
  },
  {
    "id": 110051,
    "holder": 100502,
    "name": "Ravissant",
    "targetSpeed": 0.45,
    "duration": 4,
    "check": "function () {
      return thiz.isInFinalCorner() || thiz.isInFinalStraight();
    }",
  },
  {
    "id": 110201,
    "holder": 102002,
    "name": "Do Ya Breakin!",
    "targetSpeed": 0.35,
    "acceleration": 0.2,
    "duration": 5,
    "conditions": {
      "phase": ">=2",
      "corner": 0,
      "straight_front_type": 2,
    },
  },
  {
    "id": 100511,
    "holder": 105101,
    "name": "つぼみ、ほころぶ時",
    "acceleration": 0.4,
    "duration": 4,
    "check": "function () {
      return (
        (thiz.isPhase(2) || thiz.isPhase(3)) &&
        (thiz.isInFinalStraight() ||
          thiz.isInFinalCorner(thiz.position, { \"start\": 0.5, \"end\": 1 }))
      );
    }",
  },
  {
    "id": 100721,
    "holder": 107201,
    "name": "烈火繚乱、無敵之舞",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "<=40%(1～4位)",
    "check": "function (startPosition) {
      return (
        startPosition <= thiz.toPosition(300) &&
        thiz.position >= thiz.toPosition(300)
      );
    }",
  },
  {
    "id": 110601,
    "holder": 106002,
    "name": "Go☆Go☆for it!",
    "targetSpeed": 0.35,
    "duration": 6,
    "tooltip": ">=40% <=70%(4～6位)、4人気以下",
    "check": "function () {
      return thiz.isInFinalStraight();
    }",
  },
  {
    "id": 110611,
    "holder": 106102,
    "name": "轟！トレセン応援団！！",
    "acceleration": 0.4,
    "duration": 4,
    "tooltip": "距離50%まで6～9位",
    "check": "function () {
      return thiz.isInFinalStraight() && thiz.temptationModeStart == null;
    }",
  },
  {
    "id": 100311,
    "holder": 103101,
    "name": "チャージ完了！全速前進！",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "東京時、1～2位",
    "check": "function (startPosition) {
      return (
        startPosition <= thiz.toPosition(300) &&
        thiz.position >= thiz.toPosition(300)
      );
    }",
  },
  {
    "id": 100641,
    "holder": 106401,
    "name": "ぶっちぎりロード",
    "heal": 550,
    "targetSpeed": 0.25,
    "duration": 6,
    "tooltip": "距離50%までずっと1～2位",
    "check": "function () {
      return thiz.position >= thiz.courseLength * 0.5;
    }",
  },
  {
    "id": 110221,
    "holder": 102202,
    "name": "Best day ever",
    "targetSpeed": 0.35,
    "acceleration": 0.1,
    "duration": 5,
    "tooltip": "2～4位。加速力は残り401m以上がある場合（自動で判断していない）",
    "check": "function () {
      return thiz.currentPhase >= 2 && thiz.isInFinalCorner();
    }",
  },
  {
    "id": 110381,
    "holder": 103802,
    "name": "One True Color",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "duration": 5,
    "tooltip": "2～4位、後ろ１馬身。",
    "check": "function (startPosition) {
      return thiz.isContainsRemainingDistance(350, startPosition);
    }",
  },
  {
    "id": 100341,
    "holder": 103401,
    "name": "快走かな、快走かな！",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "最終直線ランダム発動として扱う",
    "init": "function () {
      thiz.randoms = thiz.initFinalStraightRandom();
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
  },
  {
    "id": 110101,
    "holder": 101002,
    "name": "Joyful Voyage!",
    "targetSpeed": 0.35,
    "speedWithDecel": 0.15,
    "duration": 5,
    "tooltip": "2～4位。追加条件は満たしたとしてみなす。",
    "conditions": {
      "remain_distance": [199, 201],
    },
  },
  {
    "id": 110591,
    "holder": 105902,
    "name": "ときめきが呼ぶほうへ",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "4～7位。",
    "conditions": {
      "distance_rate": ">=60",
    },
    "check": "function () {
      return (
        thiz.isPhase(1) &&
        thiz.position >= thiz.courseLength * 0.6 &&
        thiz.courseLength - thiz.position >= 500 &&
        thiz.isInSlope(\"down\")
      );
    }",
  },
  {
    "id": 100361,
    "holder": 103601,
    "name": "trigger:BEAT",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "4～7位。",
    "check": "function (startPosition) {
      return thiz.isInFinalStraight(startPosition);
    }",
  },
  {
    "id": 120131,
    "holder": 101303,
    "name": "きらめくは海、まばゆきは君",
    "targetSpeed": 0.15,
    "duration": 5,
    "tooltip": "1～4位",
    "conditions": {
      "distance_rate": ">=50",
    },
    "trigger": "function (skill) {
      const map = {
        \"2000\": 1,
        \"2400\": 1.5,
        \"2600\": 2,
        \"2800\": 2.2,
        \"3000\": 2.5,
        \"3200\": 3,
        \"3500\": 3.5,
        \"99999999\": 4,
      };
      for (const key in map) {
        if (thiz.sp < key) {
          skill.durationOverwrite = skill.duration * map[key];
          return {
            \"extended\": map[key].toString(),
          };
        }
      }
    }",
  },
  {
    "id": 100441,
    "holder": 104401,
    "name": "いただき☆ストレリチア！",
    "targetSpeed": 0.35,
    "duration": 6,
    "check": "function (startPosition) {
      return (
        startPosition <= thiz.toPosition(300) &&
        thiz.position >= thiz.toPosition(300) &&
        thiz.temptationModeStart == null
      );
    }",
  },
  {
    "id": 100981,
    "holder": 109801,
    "name": "理運開かりて翔る",
    "targetSpeed": 0.25,
    "acceleration": 0,
    "duration": 5,
    "init": "function () {
      thiz.randoms = thiz.initPhaseRandom(1, { \"startRate\": 0.5 });
    }",
    "check": "function (startPosition) {
      return thiz.isInRandom(thiz.randoms, startPosition);
    }",
    "trigger": "function (skill) {
      if (skill.id === 900981) {
        skill.targetSpeed = 0.05;
        skill.acceleration = 0;
        return;
      }
      const rate = [0, 0, 0, 1, 1, 2];
      skill.targetSpeed =
        0.25 +
        (thiz.passiveTriggered > 5 ? 3 : rate[thiz.passiveTriggered]) * 0.05;
      skill.acceleration =
        (thiz.passiveTriggered > 5 ? 3 : rate[thiz.passiveTriggered]) * 0.05;
    }",
  },
  {
    "id": 110461,
    "holder": 104602,
    "name": "α-star*",
    "targetSpeed": 0.25,
    "heal": 350,
    "duration": 6,
    "conditions": {
      "ground_type": 2,
      "distance_rate": [">=40", "<=50"],
    },
    "tooltip": "MAX発動時",
  },
  {
    "id": 110351,
    "holder": 103502,
    "name": "夢の先へ、届け！",
    "targetSpeed": 0.35,
    "duration": 6,
    "tooltip": "面倒なので0.35として計算。",
    "check": "function () {
      return thiz.isInFinalStraight();
    }",
  },
  {
    "id": 110501,
    "holder": 105002,
    "name": "Hephaistos",
    "targetSpeed": 0.35,
    "duration": 5,
    "check": "function () {
      return (
        thiz.isInFinalCorner(thiz.position, { \"start\": 0.5, \"end\": 1 }) &&
        (thiz.isPhase(2) || thiz.isPhase(3))
      );
    }",
  },
  {
    "id": 100291,
    "holder": 102901,
    "name": "ゆきあかり、おいかけて",
    "targetSpeed": 0.35,
    "duration": 5,
    "tooltip": "300m即発動として扱う。実際は先頭か先頭と5m以内の差で1～4位。",
    "check": "function (startPosition) {
      return (
        startPosition <= thiz.toPosition(300) &&
        thiz.position >= thiz.toPosition(300)
      );
    }",
  },
  {
    "id": 100421,
    "holder": 104201,
    "name": "『I’m possible』",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "2～9位、先頭と5m以内扱い",
    "check": "function (startPosition) {
      return (
        startPosition <= thiz.toPosition(200) &&
        thiz.position >= thiz.toPosition(200)
      );
    }",
  },
  {
    "id": 110191,
    "holder": 101902,
    "name": "萌到讓我活過來了！",
    "targetSpeed": 0.35,
    "duration": 5,
    "conditions": {
      "phase": 1,
      "corner": 1,
    },
  },
  {
    "id": 110581,
    "holder": 105802,
    "name": "Spooky-Scary-Happy",
    "targetSpeed": 0.35,
    "duration": 5,
    "check": "function () {
      return thiz.isInFinalStraight();
    }",
  },
  {
    "id": 100871,
    "holder": 108701,
    "name": "silent letter",
    "targetSpeed": 0.25,
    "acceleration": 0.3,
    "duration": 5,
    "tooltip": "400m即発動として扱う",
    "check": "function (startPosition) {
      return (
        startPosition <= thiz.toPosition(400) &&
        thiz.position >= thiz.toPosition(400)
      );
    }",
  },
  {
    "id": 100781,
    "holder": 107801,
    "name": "風光る",
    "acceleration": 0.2,
    "duration": 8,
    "tooltip": "2位時",
    "check": "function () {
      return thiz.isInFinalCorner(thiz.position, { \"start\": 0.5, \"end\": 1 });
    }",
  },
  {
    "id": 110211,
    "holder": 102102,
    "name": "火神鳴",
    "targetSpeed": 0.3,
    "duration": 6,
    "tooltip": "2スキルで発動の即0.3として扱う",
    "check": "function () {
      return thiz.skillTriggerCount[1] >= 2;
    }",
  },
  {
    "id": 110341,
    "holder": 103402,
    "name": "灯穂",
    "targetSpeed": 0.385,
    "duration": 5,
    "tooltip": "0.385として扱う",
    "conditions": {
      "is_finalcorner": 1,
      "corner": 0,
    },
  },
  {
    "id": 100491,
    "holder": 104901,
    "name": "剣ヶ峰より、狂気に嗤え",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "0.45として扱う",
    "check": "function () {
      return thiz.remainDistance <= 400;
    }",
  },
  {
    "id": 101001,
    "holder": 110001,
    "name": "Never Say Never",
    "speedWithDecel": 0.25,
    "duration": 5,
    "conditions": {
      "remain_distance": [299, 301],
      "ground_type": 2,
    },
  },
  {
    "id": 120011,
    "holder": 100103,
    "name": "威風堂々、夢錦！",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "中山の0.45として扱う",
    "conditions": {
      "phase": ">=2",
      "is_finalcorner": 1,
      "corner": 1,
      "is_activate_any_skill": 1,
      "track_id": 10005,
    },
  },
  {
    "id": 110091,
    "holder": 100902,
    "name": "Queen’s Lumination",
    "targetSpeed": 0.35,
    "duration": 6,
    "tooltip": "0.35のみ",
    "conditions": {
      "distance_rate": ">=50",
      "corner": 0,
    },
  },
  {
    "id": 110081,
    "holder": 100802,
    "name": "Into High Gear!",
    "targetSpeed": 0.35,
    "acceleration": 0.1,
    "duration": 5,
    "tooltip": "常に東京として扱う。分ける実装面倒すぎぃ！",
    "check": "function (startPosition) {
      return (
        thiz.isInSlope(\"down\", startPosition) &&
        !thiz.isInSlope(\"down\") &&
        thiz.phase >= 1
      );
    }",
  },
  {
    "id": 100471,
    "holder": 104701,
    "name": "掲げよ、己が魂の剣を！",
    "targetSpeed": 0.45,
    "duration": 5,
    "tooltip": "0.45のみ",
    "conditions": {
      "remain_distance": 400,
    },
  },
  {
    "id": 110161,
    "holder": 101602,
    "name": "灰色の臨界点",
    "targetSpeed": 0.55,
    "duration": 5,
    "tooltip": "0.55のみ",
    "conditions": {
      "distance_type": 4,
      "phase": ">=2",
      "is_finalcorner_laterhalf": 1,
    },
  },
  {
    "id": 110671,
    "holder": 106702,
    "name": "玄雲散らす、黄金甲矢",
    "targetSpeed": 0.35,
    "heal": 350,
    "duration": 5,
    "conditions": {
      "distance_rate": [40, 50],
    },
  },
  {
    "id": 110681,
    "holder": 106802,
    "name": "あっぱれ大盤振る舞い！",
    "speedWithDecel": 0.35,
    "acceleration": 0.1,
    "duration": 4,
    "conditions": {
      "phase": ">=2",
      "corner": 1,
      "remain_distance": ">=600",
    },
  },
  {
    "id": 100991,
    "holder": 109901,
    "name": "かがやけ☆とまこまい",
    "acceleration": 0.4,
    "duration": 4,
    "tooltip": "最大スパート時のみ、3～4位＆中盤コーナーで競り合い",
    "conditions": {
      "ground_type": 2,
      "lastspurt": 2,
      "is_lastspurt": 1,
    },
  },
  {
    "id": 100651,
    "holder": 106501,
    "name": "アゲてアゲてぷちょへんざ！",
    "targetSpeed": 0.25,
    "duration": 6,
    "tooltip": "短距離/マイルのみ、順位<=50%",
    "conditions": {
      "distance_type": [1, 2],
      "phase_laterhalf_random": 1,
    },
  },
  {
    "id": 110271,
    "holder": 102702,
    "name": "あなたに捧げるフリーポア",
    "targetSpeed": 0.35,
    "acceleration": 0.1,
    "duration": 4,
    "tooltip": "中距離のみ、>=2位",
    "conditions": {
      "distance_rate": ">=60",
      "slope": 2,
      "phase": 1,
      "distance_type": 3,
    },
  },
  {
    "id": 110311,
    "holder": 103102,
    "name": "フレッシュ☆パーラー",
    "targetSpeed": 0.25,
    "heal": 350,
    "duration": 6,
    "tooltip": "順位<=30%",
    "conditions": {
      "phase_firsthalf_random": 1,
    },
  },
  {
    "id": 100431,
    "holder": 104301,
    "name": "Ding Dong Boo",
    "targetSpeed": 0.25,
    "acceleration": 0.4,
    "duration": 5,
    "tooltip": "ダートのみ、最終直線ランダム発動として扱う",
    "conditions": {
      "ground_type": 2,
      "is_finalstraight_random": 1,
    },
  },
  {
    "id": 100571,
    "holder": 105701,
    "name": "叙情、旅路の果てに",
    "targetSpeed": 0.35,
    "duration": 6,
    "tooltip": "強化版のみ。6～9位で先頭から8馬身差。",
    "conditions": {
      "distance_type": [3, 4],
      "phase_laterhalf_random": 1,
    },
  },
  {
    "id": 100661,
    "holder": 106601,
    "name": "エンジン全開！大噴射！",
    "type": "speed",
    "invokes": [
      {
        "invokeNo": 1,
        "targetSpeed": 0.15,
        "duration": 13,
        "conditions": {
          "distance_rate": [34, 36],
        },
      },
      {
        "invokeNo": 2,
        "speedWithDecel": -0.05,
        "duration": 500,
        "conditions": {
          "phase": 3,
        },
      },
    ],
  },
  {
    "id": 100851,
    "holder": 108501,
    "name": "至上であれ",
    "speedWithDecel": 0.25,
    "duration": 5,
    "conditions": {
      "distance_rate": [66, 68],
      "temptation_count": 0,
    },
  },
  {
    "id": 100531,
    "holder": 105301,
    "name": "熱血！！風紀アタック",
    "acceleration": 0.3,
    "duration": 5,
    "conditions": {
      "phase": ">=2",
      "is_finalcorner": 1,
    },
    "tooltip": "MAXっぽい5秒の0.3加速として扱う",
  },
  {
    "id": 110071,
    "holder": 100702,
    "name": "Adventure of 564",
    "targetSpeed": 0.15,
    "duration": 5,
    "conditions": {
      "distance_rate_random": [50, 100],
    },
    "trigger": "function (thisSkill) {
      const candidates = [];
      for (const skill of thiz.invokedSkills) {
        if (
          !thiz.isInCoolDown(skill) &&
          [\"rare\", \"evo\"].includes(skill.rarity)
        ) {
          candidates.push(skill);
        }
      }
      const chainTriggered = [];
      const num = thisSkill.type === \"unique\" ? 2 : 1;
      for (let i = 0; i < num && candidates.length > 0; i++) {
        const index = Math.floor(Math.random() * candidates.length);
        chainTriggered.push(candidates[index]);
        candidates.splice(index, 1);
      }
      return { chainTriggered };
    }",
  },
  {
    "id": 100831,
    "holder": 108301,
    "name": "Mission: Triumph",
    "targetSpeed": 0.35,
    "invokes": [
      {
        "duration": 6,
        "conditions": {
          "distance_rate": ">=40",
          "corner": 0,
          "slope": 2,
        },
      },
      {
        "duration": 5,
        "conditions": {
          "distance_rate": ">=40",
          "corner": 0,
        },
      },
    ],
  },
  {
    "id": 110711,
    "holder": 107102,
    "name": "Danser le présent",
    "targetSpeed": 0.35,
    "speedWithDecel": 0.15,
    "heal": -300,
    "duration": 5,
    "tooltip": "順位>=20%,<=40%",
    "conditions": {
      "phase": ">=2",
      "is_finalcorner": 1,
      "corner": 0,
    },
  },
  {
    "id": 110691,
    "holder": 106902,
    "name": "咲け咲け！私！",
    "acceleration": 0.4,
    "duration": 5,
    "tooltip": "順位>=30%,<=40%",
    "conditions": {
      "remain_distance": [649, 651],
    },
  },
  {
    "id": 100761,
    "holder": 107601,
    "name": "花開き、世界",
    "invokes": [
      {
        "heal": 750,
        "conditions": {
          "distance_type": 4,
          "distance_rate": [50, 51],
        },
      },
      {
        "conditions": {
          "distance_rate": [50, 51],
        },
      },
    ],
    "targetSpeed": 0.25,
    "duration": 6,
    "type": "speed",
    "tooltip": "順位>=30%,<=80%(3-7)",
  },
  {
    "id": 101051,
    "holder": 110501,
    "name": "アド・アストラ",
    "invokes": [
      {
        "targetSpeed": 0.45,
        "conditions": {
          "distance_type": 3,
          "distance_rate": [50, 55],
          "activate_count_all": 7,
        },
      },
      {
        "targetSpeed": 0.35,
        "conditions": {
          "distance_rate": [50, 55],
        },
      },
    ],
    "type": "speed",
    "duration": 5,
    "tooltip": "順位>=30%,<=80%(3-7)",
  },
  {
    "id": 110621,
    "holder": 106202,
    "name": "ごろりん！？パワードライブ",
    "invokes": [
      {
        "targetSpeed": 0.25,
        "acceleration": 0.1,
        "conditions": {
          "distance_type": 3,
          "distance_rate": ">=50",
          "corner": 1,
        },
      },
      {
        "targetSpeed": 0.25,
        "conditions": {
          "distance_rate": ">=50",
          "corner": 1,
        },
      },
    ],
    "duration": 6,
    "tooltip": "6秒扱い",
    "type": "composite",
  },
  {
    "id": 110411,
    "holder": 104102,
    "name": "CHERRY☆スクランブル",
    "acceleration": 0.4,
    "conditions": {
      "remain_distance": [399, 401],
    },
    "duration": 4,
    "tooltip": "4秒扱い",
  },
  {
    "id": 101061,
    "holder": 110601,
    "name": "Bang☆ミラクるわせ！",
    "targetSpeed": 0.45,
    "conditions": {
      "distance_type": 4,
      "is_finalcorner_laterhalf": 1,
    },
    "duration": 6,
    "tooltip": "4番人気以下時。>=40%,<=70%",
  },
  {
    "id": 100841,
    "holder": 108401,
    "name": "霹靂のアウフヘーベン",
    "invokes": [
      {
        "speedWithDecel": 0.55,
        "conditions": {
          "phase": ">=2",
          "is_finalcorner": 1,
          "corner": 0,
          "distance_type": 3,
          "track_id": 10006,
        },
      },
      {
        "speedWithDecel": 0.35,
        "conditions": {
          "phase": ">=2",
          "is_finalcorner": 1,
          "corner": 0,
        },
      },
    ],
    "type": "speed",
    "duration": 5,
  },
  {
    "id": 110511,
    "holder": 105102,
    "name": "Flowering Dreams",
    "targetSpeed": 0.35,
    "speedWithDecel": 0.15,
    "conditions": {
      "remain_distance": [199, 201],
    },
    "duration": 5,
    "tooltip": "2～<=70%。近くに3人条件は満たしているとみなす",
  },
  {
    "id": 110121,
    "holder": 101202,
    "name": "大盛り！ファーストバイト！",
    "targetSpeed": 0.35,
    "acceleration": 0.1,
    "conditions": {
      "distance_rate": ">=60",
      "phase": 1,
      "slope": 2,
    },
    "duration": 4,
    "tooltip": "中盤追い越し3回のあと、順位>=50%",
  },
  {
    "id": 100551,
    "holder": 105501,
    "name": "万彩☆マーベラス★世界",
    "targetSpeed": 0.4,
    "conditions": {
      "distance_rate": [40, 50],
    },
    "duration": 5,
    "tooltip": "0.4扱い",
  },
  {
    "id": 110531,
    "holder": 105302,
    "name": "奥義・常夏バーニング！！",
    "targetSpeed": 0.45,
    "conditions": {
      "remain_distance": [199, 201],
    },
    "duration": 5,
    "tooltip": "0.45扱い",
  },
  {
    "id": 110481,
    "holder": 104802,
    "name": "GALmem．ふぉーえば♪",
    "targetSpeed": 0.3,
    "conditions": {
      "distance_rate": [40, 50],
    },
    "duration": 6,
    "tooltip": "0.3扱い",
  },
  {
    "id": 101041,
    "holder": 110401,
    "name": "暁の御旗『葛城栄主』！",
    "targetSpeed": 0.25,
    "heal": 350,
    "conditions": {
      "corner": 3,
      "distance_type": 3,
      "track_id": 10006,
    },
    "duration": 6,
    "tooltip": "6秒扱い",
  },
  {
    "id": 100701,
    "holder": 107001,
    "name": "セイリオス",
    "invokes": [
      {
        "acceleration": 0.5,
        "duration": 4,
        "conditions": {
          "remain_distance": [799, 801],
          "course_distance": 2400,
        },
      },
      {
        "acceleration": 0.2,
        "duration": 4,
        "conditions": {
          "remain_distance": [799, 801],
        },
      },
    ],
    "type": "acceleration",
    "tooltip": "3番人気以上前提",
  },
  {
    "id": 110321,
    "holder": 103202,
    "name": "夏空ハレーション",
    "targetSpeed": 0.45,
    "duration": 4,
    "conditions": {
      "distance_rate": ">=50",
      "corner": 1,
      "track_id": 10005,
    },
    "tooltip": "加速度なし扱い",
  },
  {
    "id": 110021,
    "holder": 100202,
    "name": "水平線のその先へ",
    "invokes": [
      {
        "speedWithDecel": 0.45,
        "duration": 5,
        "conditions": {
          "distance_rate": [66, 68],
          "corner": 3,
        },
      },
      {
        "speedWithDecel": 0.25,
        "duration": 5,
        "conditions": {
          "distance_rate": [66, 68],
        },
      },
    ],
    "type": "speed",
  },
  {
    "id": 100771,
    "holder": 107701,
    "name": "Road to Glory",
    "duration": 5,
    "invokes": [
      {
        "targetSpeed": 0.55,
        "conditions": {
          "distance_type": 4,
          "remain_distance": [399, 401],
        },
      },
      {
        "targetSpeed": 0.45,
        "conditions": {
          "remain_distance": [399, 401],
        },
      },
    ],
    "type": "speed",
    "tooltip": "先頭と1馬身以内は満たしているとして扱う",
  },
  {
    "id": 120071,
    "holder": 100703,
    "name": "Vive la GOLD",
    "duration": 5,
    "invokes": [
      {
        "targetSpeed": 0.45,
        "conditions": {
          "distance_type": [3, 4],
          "is_finalcorner": 1,
          "is_badstart": 1,
        },
      },
      {
        "targetSpeed": 0.35,
        "conditions": {
          "distance_type": [3, 4],
          "is_finalcorner": 1,
        },
      },
    ],
    "type": "speed",
    "tooltip": "先頭と1馬身以内は満たしているとして扱う",
  },
  {
    "id": 120671,
    "holder": 106703,
    "name": "ポンテ・デ・ディアマン",
    "duration": 5,
    "invokes": [
      {
        "targetSpeed": 0.35,
        "conditions": {
          "phase_laterhalf_random": 1,
          "distance_type": 3,
        },
      },
      {
        "targetSpeed": 0.25,
        "conditions": {
          "phase_laterhalf_random": 1,
        },
      },
    ],
    "type": "speed",
  },
  {
    "id": 100931,
    "holder": 109301,
    "name": "幸せの青い光",
    "duration": 5,
    "invokes": [
      {
        "speedWithDecel": 0.35,
        "conditions": {
          "phase_firsthalf_random": 2,
          "distance_type": [1, 2],
        },
      },
      {
        "speedWithDecel": 0.25,
        "conditions": {
          "phase_firsthalf_random": 2,
          "distance_type": [3, 4],
        },
      },
    ],
    "type": "speed",
  },
  {
    "id": 110831,
    "holder": 108302,
    "name": "Immortal Work",
    "duration": 5,
    "invokes": [
      {
        "targetSpeed": 0.55,
        "conditions": {
          "is_last_straight": 1,
          "track_id": 10005,
          "distance_type": 3,
        },
      },
      {
        "speedWithDecel": 0.35,
        "conditions": {
          "is_last_straight": 1,
        },
      },
    ],
    "type": "speed",
  },
  {
    "id": 110361,
    "holder": 103602,
    "name": "…found you．",
    "duration": 5,
    "targetSpeed": 0.45,
    "conditions": {
      "phase": ">=2",
      "is_finalcorner": 1,
      "corner": 0,
    },
    "tooltip": "0.45の即時発動扱い",
  },
  {
    "id": 100861,
    "holder": 108601,
    "name": "愛と熔けよただ熔けよ",
    "duration": 5,
    "targetSpeed": 0.35,
    "conditions": {
      "remain_distance": [999, 1001],
    },
  },
]
""".trimIndent()