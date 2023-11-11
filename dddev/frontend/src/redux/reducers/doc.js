import { SET_DOC } from 'redux/actions/doc';

const initialState = {
  docTitle: null,
};

const docReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_DOC:
      return {
        ...state,
        docTitle: action.payload.docTitle,
      };
    default:
      return state;
  }
};

export default docReducer;
