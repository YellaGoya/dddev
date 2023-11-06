import { TOGGLE_MENU, SET_MENU, SET_MESSAGE } from 'redux/actions/menu';

const initialState = {
  menuToggle: false,
  messageToggle: false,
};

const menuReducer = (state = initialState, action) => {
  switch (action.type) {
    case TOGGLE_MENU:
      return { ...state, menuToggle: !state.menuToggle };
    case SET_MENU:
      return { ...state, menuToggle: action.payload };
    case SET_MESSAGE:
      return { ...state, messageToggle: action.payload };
    default:
      return state;
  }
};

export default menuReducer;
