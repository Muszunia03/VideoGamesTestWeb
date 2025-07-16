import requests
import psycopg2

API_KEY = 'c323242a2ed74d6ba0e15bda9d3ad394'
API_URL = 'https://api.rawg.io/api/games'

DB_CONFIG = {
    'dbname': 'postgres',
    'user': 'postgres',
    'password': 'admin',
    'host': 'localhost',
    'port': '5432'
}

MAX_GAMES = 1000

def fetch_games(page_size=40, max_games=MAX_GAMES):
    all_games = []
    page = 1
    while len(all_games) < max_games:
        params = {
            'key': API_KEY,
            'page_size': page_size,
            'page': page
        }
        response = requests.get(API_URL, params=params)
        response.raise_for_status()
        data = response.json()
        all_games.extend(data['results'])

        if len(data['results']) < page_size:
            break

        page += 1

        if len(all_games) >= max_games:
            break

    return all_games[:max_games]

def save_game_to_db(conn, game):
    with conn.cursor() as cur:
        cur.execute("""
            INSERT INTO games (external_id, title, release_date, description, background_image, rating, genres, platforms)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
            ON CONFLICT (external_id) DO NOTHING;
        """, (
            game.get('id'),
            game.get('name'),
            game.get('released'),
            game.get('slug'),
            game.get('background_image'),
            game.get('rating'),
            [genre['name'] for genre in game.get('genres', [])], 
            [platform['platform']['name'] for platform in game.get('platforms', [])] 
        ))

        conn.commit()

def main():
    games = fetch_games(max_games=MAX_GAMES)
    print(f"Pobrano {len(games)} gier.")

    conn = psycopg2.connect(**DB_CONFIG)

    for game in games:
        print(f"Zapisuję grę: {game['name']}")
        save_game_to_db(conn, game)

    conn.close()
    print("Gotowe!")

if __name__ == '__main__':
    main()
