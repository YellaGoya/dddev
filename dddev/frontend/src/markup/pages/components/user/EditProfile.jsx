import { useState, useRef } from 'react';
import { useSelector } from 'react-redux';
import eetch from 'eetch/eetch';
import Input from 'markup/pages/components/common/Input';

import userStockImage from 'assets/userStockImage.webp';
import DisabledByDefaultRoundedIcon from '@mui/icons-material/DisabledByDefaultRounded';
import PhotoFilterIcon from '@mui/icons-material/PhotoFilter';

import * as s from 'markup/styles/components/user/EditProfile';
const EditProfile = ({ toggle, setToggle, userInfo, setUserInfo }) => {
  const user = useSelector((state) => state.user);
  const [pat, setPat] = useState((userInfo.personalAccessToken && userInfo.personalAccessToken.substr(0, 3).concat(' ···')) || '');
  const [nickname, setNickname] = useState(userInfo.nickname || '');
  const [statusMsg, setStatusMsg] = useState(userInfo.statusMsg || '');
  const [patMessage, setPatMessage] = useState(null);
  const hiddenFileInput = useRef(null);

  const handleFileSelect = () => {
    hiddenFileInput.current.click();
  };

  const handleChange = (event) => {
    const formData = new FormData();
    formData.append('file', event.target.files[0]);

    eetch
      .userUploadImage({ Authorization: user.accessToken, formData })
      .then(() => {
        eetch
          .userInfo({ Authorization: user.accessToken })
          .then((res) => {
            setUserInfo(res.data);
          })
          .catch((err) => {
            console.log(err);
          });
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const submitPat = () => {
    eetch
      .githubTokenRegist({ Authorization: user.accessToken, personalAccessToken: pat })
      .then(() => {
        setPatMessage('통과');
      })
      .catch(() => {
        setPatMessage('틀려먹음');
      });
  };

  const submitChange = () => {
    eetch.userEdit({ Authorization: user.accessToken, nickname, statusMsg }).catch((err) => {
      console.log(err);
    });
  };

  const deleteImage = () => {
    eetch.userDeleteImage({ Authorization: user.accessToken }).catch((err) => {
      console.log(err);
    });
  };

  return (
    <>
      <s.OutWrapper $toggle={toggle} onClick={() => setToggle(false)} />
      <s.EditWrapper $toggle={toggle}>
        <s.EditModalWrapper>
          유저 프로필 수정이 들어갈 곳
          <s.EditImageWrapper>
            <s.ProfileImage src={userInfo.profileDto ? `https://k9d103.p.ssafy.io/img/user/${userInfo.profileDto.fileName}` : userStockImage} />
            <s.ImageButton onClick={handleFileSelect}>
              <PhotoFilterIcon />
            </s.ImageButton>
            <s.ImageButton onClick={deleteImage}>
              <DisabledByDefaultRoundedIcon />
            </s.ImageButton>
            <input ref={hiddenFileInput} type="file" style={{ display: 'none' }} onChange={handleChange} />
          </s.EditImageWrapper>
          <Input label="Pat" holder={pat} setData={setPat} enter={submitPat} click={submitPat} message={patMessage} />
          <Input label="닉네임" data={nickname} setData={setNickname} />
          <Input label="상태 메시지" data={statusMsg} setData={setStatusMsg} />
          <s.ProfileEditButton type="button" onClick={submitChange}>
            변경
          </s.ProfileEditButton>
          <p onClick={() => setToggle(false)}>close</p>
        </s.EditModalWrapper>
      </s.EditWrapper>
    </>
  );
};

export default EditProfile;
