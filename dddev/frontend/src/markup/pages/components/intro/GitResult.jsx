import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { loginUser } from 'redux/actions/user';

const GitResult = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [code, setCode] = useState('');
  useEffect(() => {
    const url = window.location.href;
    setCode(url.split('=')[1]);

    dispatch(loginUser({ accessToken: 323, refreshToken: 42424 }));

    navigate('/');
  }, [dispatch, navigate]);

  return <div>GitResult {code}</div>;
};

export default GitResult;
