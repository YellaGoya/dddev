import { LOGIN_USER, LOGOUT_USER, UPDATE_TOKENS } from 'redux/actions/user';

const initialState = {
  isLoggedIn: false,
  accessToken: null,
  accessExp: null,
  refreshToken: null,
  lastGround: null,
};

const userReducer = (state = initialState, action) => {
  switch (action.type) {
    case LOGIN_USER:
      return {
        ...state,
        isLoggedIn: true,
        accessToken: action.payload.accessToken,
        accessExp: action.payload.accessExp,
        refreshToken: action.payload.refreshToken,
        lastGround: action.payload.lastGround,
      };

    case LOGOUT_USER:
      return initialState;

    case UPDATE_TOKENS:
      return {
        ...state,
        accessToken: action.payload.accessToken || state.accessToken,
        accessExp: action.payload.accessExp || state.accessExp,
        refreshToken: action.payload.refreshToken || state.refreshToken,
        lastGround: action.payload.lastGround || state.lastGround,
      };

    default:
      return state;
  }
};

export default userReducer;
