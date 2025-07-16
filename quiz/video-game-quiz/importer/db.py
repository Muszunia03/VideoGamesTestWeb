import subprocess
import time
import os
import psycopg2

# Konfiguracja bazy danych
PG_DATA_PATH = 'C:/Program Files/PostgreSQL/17/data'  # Zamień na ścieżkę do folderu danych PostgreSQL
PG_BIN_PATH = 'C:/Program Files/PostgreSQL/17/bin'  # Zamień na ścieżkę do folderu bin w PostgreSQL
PG_PORT = 5432  # Port bazy danych
PG_USER = 'postgres'  # Użytkownik bazy danych
PG_PASSWORD = 'admin'  # Hasło bazy danych
PG_DB = 'postgres'  # Nazwa bazy danych

# Funkcja uruchamiająca PostgreSQL
def start_postgresql():
    print("Uruchamiam PostgreSQL...")
    try:
        # Uruchamianie PostgreSQL w tle
        subprocess.Popen([os.path.join(PG_BIN_PATH, 'pg_ctl'), 'start', '-D', PG_DATA_PATH, '-l', 'logfile'])
        time.sleep(10)  # Dajemy czas na uruchomienie bazy
        print("PostgreSQL uruchomiony.")
    except Exception as e:
        print(f"Nie udało się uruchomić PostgreSQL: {e}")

# Funkcja łącząca się z bazą danych
def connect_to_database():
    try:
        # Połączenie z bazą danych
        connection = psycopg2.connect(
            dbname=PG_DB,
            user=PG_USER,
            password=PG_PASSWORD,
            host='localhost',
            port=PG_PORT
        )
        print("Połączono z bazą danych.")
        return connection
    except Exception as e:
        print(f"Nie udało się połączyć z bazą danych: {e}")
        return None

# Funkcja uruchamiająca aplikację
def run_app():
    print("Uruchamiam aplikację...")
    # Tu uruchamiasz swoją aplikację, np.:
    # subprocess.Popen(['python', 'app.py'])
    # Dla prostoty załóżmy, że aplikacja to połączenie z bazą danych
    conn = connect_to_database()
    if conn:
        # Przykładowe operacje na bazie danych, np. sprawdzenie połączenia
        with conn.cursor() as cur:
            cur.execute("SELECT version();")
            db_version = cur.fetchone()
            print(f"Połączono z bazą: {db_version}")
        conn.close()
    else:
        print("Nie udało się uruchomić aplikacji bez połączenia z bazą danych.")

# Uruchamianie PostgreSQL i aplikacji
if __name__ == "__main__":
    start_postgresql()  # Uruchamiamy PostgreSQL
    run_app()  # Uruchamiamy aplikację po połączeniu
