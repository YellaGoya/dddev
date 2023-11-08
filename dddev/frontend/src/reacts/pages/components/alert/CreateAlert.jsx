// import eetch from 'eetch/eetch';
import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import Select from 'reacts/pages/components/common/Select';
import Input from 'reacts/pages/components/common/Input';
import eetch from 'eetch/eetch';

const CreateAlert = () => {
  const user = useSelector((state) => state.user);
  // const [keywordMessage, setKeywordMessage] = useState({fail: false, text: ''});
  const [type, setType] = useState(null);
  const [grounds, setGrounds] = useState([]);
  const [ground, setGround] = useState(null);
  const [keywords, setKeywords] = useState([]);

  useEffect(() => {
    eetch
      .userGroundsExample({ accessToken: user.accessToken, refreshToken: user.refreshToken })
      .then((res) => {
        // console.log(res.data);
        setGrounds(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [user.accessToken, user.refreshToken]);

  useEffect(() => {
    console.log(keywords);
  }, [keywords]);

  const submitAlert = () => {
    console.log(ground);
    eetch.createAlert({ accessToken: user.accessToken, refreshToken: user.refreshToken, repoId: 0, keyword: keywords, type }).catch((err) => {
      console.log(err);
    });
  };

  return (
    <div>
      <Select label="ground" list={grounds} select={setGround} />
      <Select label="type" list={['push', 'pull_request']} select={setType} />
      <div type="button" onClick={submitAlert}>
        추가
      </div>
      <Input label="테스트용" array={keywords} enter={setKeywords} />
    </div>
  );
};

export default CreateAlert;
