import { LOGIN_USER, LOGOUT_USER, UPDATE_TOKENS } from 'redux/actions/user';

const initialState = {
  isLoggedIn: true,
  accessToken: 'test',
  refreshToken: 'test',
  lastGround: 'test1',
};

const userReducer = (state = initialState, action) => {
  switch (action.type) {
    case LOGIN_USER:
      return {
        ...state,
        isLoggedIn: true,
        accessToken: action.payload.accessToken,
        refreshToken: action.payload.refreshToken,
        lastGround: action.payload.lastGround,
      };

    case LOGOUT_USER:
      return initialState;

    case UPDATE_TOKENS:
      return {
        ...state,
        accessToken: action.payload.accessToken || state.accessToken,
        refreshToken: action.payload.refreshToken || state.refreshToken,
        lastGround: action.payload.lastGround || state.lastGround,
      };

    default:
      return state;
  }
};

export default userReducer;
