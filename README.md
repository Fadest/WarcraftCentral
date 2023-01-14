# WarcraftCentral

## Introduction

This application is built as the backend of a web application to check World of Warcraft's PvP statistics.

## How to use

### OAuth2 Configuration

You need a `clientId` and `clientSecret` that will authenticate to Blizzard in order to use their API.

Both of them can be [generated here](https://develop.battle.net/documentation/guides/getting-started), follow the steps in that link and
you'll be setup.

### How to compile

```
gradlew build
```

### Environment Variables

| Name                       | Value                   |
|----------------------------|-------------------------|
| BLIZZARD_API_CLIENT_ID     | Blizzard API Client ID  |
| BLIZZARD_API_CLIENT_SECRET | Blizzard API Client secret |
| POSTGRES_HOST              | Postgres Host           |
| POSTGRES_USER              | Postgres Username       |
| POSTGRES_PASSWORD          | Postgres Password       |

### Endpoints

#### /characters/{region}/{realmSlug}/{name} [GET]

Returns a Character from Blizzard's API, saving it to database and grabbing additional information.

**Params**

- update (optional): Force update the character and return it

**Response**

```json
{
  "id": 228565007,
  "name": "Fadest",
  "region": "us",
  "realm": "Quel'Thalas",
  "realmSlug": "quelthalas",
  "faction": "HORDE",
  "race": 2,
  "currentClass": 6,
  "currentSpecialization": 252,
  "lastUpdate": 1673644238101,
  "pvp": {
    "brackets": [
      {
        "bracket": "SOLO_SHUFFLE",
        "specialization": 252,
        "wins": 2,
        "loses": 0,
        "rating": 576,
        "maxRating": 0
      },
      {
        "bracket": "TWO_VERSUS_TWO",
        "specialization": 0,
        "wins": 0,
        "loses": 0,
        "rating": 0,
        "maxRating": 0
      },
      {
        "bracket": "THREE_VERSUS_TREE",
        "specialization": 0,
        "wins": 0,
        "loses": 0,
        "rating": 0,
        "maxRating": 1752
      },
      {
        "bracket": "BATTLEGROUNDS",
        "specialization": 0,
        "wins": 0,
        "loses": 0,
        "rating": 0,
        "maxRating": 0
      }
    ],
    "honorLevel": 32,
    "totalHonorableKills": 3187
  }
}
```

#### /characters/search [GET]

Searchs a character on the database using the given query

**Params**

- query: The name and/or realm to search for

**Response**

```json
[
  {
    "name": "Doodles",
    "realm": "Ragnaros",
    "faction": "HORDE",
    "race": 5,
    "currentClass": 5,
    "currentSpecialization": 256
  },
  {
    "name": "Hitmaan",
    "realm": "Ragnaros",
    "faction": "HORDE",
    "race": 10,
    "currentClass": 1,
    "currentSpecialization": 73
  },
  {
    "name": "Fadest",
    "realm": "Quel'Thalas",
    "faction": "HORDE",
    "race": 2,
    "currentClass": 6,
    "currentSpecialization": 252
  }
]
```

## Licensing

This project is licensed under the [MIT License](https://choosealicense.com/licenses/mit/).