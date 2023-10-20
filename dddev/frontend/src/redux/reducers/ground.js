import { SET_GROUND } from 'redux/actions/ground';

const initialState = {
  groundId: '101',
  groundName: 'test',
};

const groundReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_GROUND:
      return {
        ...state,
        groundId: action.payload.groundId,
        groundName: action.payload.groundName,
      };
    default:
      return state;
  }
};

export default groundReducer;
