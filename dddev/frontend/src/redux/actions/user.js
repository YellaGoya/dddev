export const LOGIN_USER = 'LOGIN_USER';
export const LOGOUT_USER = 'LOGOUT_USER';
export const UPDATE_TOKENS = 'UPDATE_TOKENS';

export const loginUser = ({ accessToken, refreshToken, lastGround }) => ({
  type: LOGIN_USER,
  payload: {
    accessToken,
    refreshToken,
    lastGround,
  },
});

export const logoutUser = () => ({
  type: LOGOUT_USER,
});

export const updateTokens = ({ accessToken, refreshToken, lastGround }) => ({
  type: UPDATE_TOKENS,
  payload: {
    accessToken,
    refreshToken,
    lastGround,
  },
});
