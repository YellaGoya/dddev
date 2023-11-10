import React from 'react';

const NotFound = ({ type }) => {
  const warningText = () => {
    switch (type) {
      case 'groundId':
        return '옳바른 그라운드가 아닙니다.';
      default:
        return '옳바른 페이지가 아닙니다.';
    }
  };

  return <div>{warningText()}</div>;
};

export default NotFound;
