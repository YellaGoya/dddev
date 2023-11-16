import { LOGIN_USER, LOGOUT_USER, UPDATE_USER } from 'redux/actions/user';

const initialState = {
  isLoggedIn: false,
  accessToken: null,
  accessExp: null,
  refreshToken: null,
  lastGround: null,
  groundsList: null,
  groundsMap: null,
  groundsMine: null,
  githubId: null,
  unread: 0,
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
        groundsList: action.payload.groundsList,
        groundsMap: action.payload.groundsMap,
        groundsMine: action.payload.groundsMine,
        githubId: action.payload.githubId,
      };

    case LOGOUT_USER:
      return initialState;

    case UPDATE_USER:
      return {
        ...state,
        accessToken: action.payload.accessToken || state.accessToken,
        accessExp: action.payload.accessExp || state.accessExp,
        refreshToken: action.payload.refreshToken || state.refreshToken,
        lastGround: action.payload.lastGround || state.lastGround,
        groundsList: action.payload.groundsList || state.groundsList,
        groundsMap: action.payload.groundsMap || state.groundsMap,
        groundsMine: action.payload.groundsMine || state.groundsMine,
        githubId: action.payload.githubId || state.githubId,
        unread: action.payload.unread || state.unread,
      };

    default:
      return state;
  }
};

export default userReducer;
