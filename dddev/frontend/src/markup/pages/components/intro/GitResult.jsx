import { useState, useEffect } from 'react';
// import { useNavigate } from 'react-router-dom';
// import { useDispatch } from 'react-redux';

import eetch from 'eetch/eetch';

// import { loginUser } from 'redux/actions/user';

const GitResult = () => {
  // const dispatch = useDispatch();
  // const navigate = useNavigate();
  const [code, setCode] = useState('');

  useEffect(() => {
    const code = window.location.href.split('=')[1];
    setCode(code);
    console.log(33);
    if (code === ``) return;
    console.log(44);

    eetch
      .githubSync({ code })
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
    // dispatch(loginUser({ accessToken: 323, refreshToken: 42424, lastGround: 'test2' }));

    // navigate('/');
  }, []);

  return <div>GitResult {code}</div>;
};

export default GitResult;
