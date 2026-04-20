const TOKEN_KEY = 'bb_access_token';

export function readStoredAccessToken(): string | null {
  return sessionStorage.getItem(TOKEN_KEY);
}

export function writeStoredAccessToken(token: string): void {
  sessionStorage.setItem(TOKEN_KEY, token);
}

export function clearStoredAccessToken(): void {
  sessionStorage.removeItem(TOKEN_KEY);
}
