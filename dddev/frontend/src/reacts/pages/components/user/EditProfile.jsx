import { useEffect, useState, useRef } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import eetch from 'eetch/eetch';
import Input from 'reacts/pages/components/common/Input';
// import Select from 'reacts/pages/components/common/SelectUser';

import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import userStockImage from 'assets/userStockImage.webp';
import DisabledByDefaultRoundedIcon from '@mui/icons-material/DisabledByDefaultRounded';
import PhotoFilterIcon from '@mui/icons-material/PhotoFilter';
import MeetingRoomRoundedIcon from '@mui/icons-material/MeetingRoomRounded';

import * as s from 'reacts/styles/components/user/EditProfile';

// const testDummy = [
//   { id: 1, name: 'test1' },
//   { id: 2, name: 'test2' },
// ];

const EditProfile = ({ toggle, setToggle, userInfo, setUserInfo }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const [pat, setPat] = useState('');
  const [nickname, setNickname] = useState(userInfo.nickname);
  const [statusMsg, setStatusMsg] = useState(userInfo.statusMsg);
  const [imgMessage, setImgMessage] = useState('');
  const [patMessage, setPatMessage] = useState({ fail: false, text: '' });
  const hiddenFileInput = useRef(null);

  const imageRef = useRef(null);
  const sizeRef = useRef(null);

  const logoutHandler = () => {
    dispatch(logoutUser());
    dispatch(setMenu(false));
    dispatch(setMessage(false));
    navigate(`/login`);
    setToggle(false);
  };

  const nicknameValid = (data, setMessage) => {
    if (data.length < 2 || data.length > 10) {
      setMessage({ fail: true, text: '* 2자 이상 10자 이하로 입력해주세요.' });
      return;
    }

    if (data.match(/[^a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]/)) {
      setMessage({ fail: true, text: '* 한글, 영문, 숫자만 입력 가능합니다.' });
      return;
    }

    eetch
      .userNickname({ accessToken: user.accessToken, refreshToken: user.refreshToken, nickname: data })
      .then((res) => {
        if (res.data) setMessage({ fail: false, text: '* 사용 가능한 닉네임입니다.' });
        else setMessage({ fail: true, text: '* 이미 존재하는 닉네임입니다.' });
      })
      .catch(() => {
        setMessage({ fail: true, text: '* 사용 불가능한 닉네임입니다.' });
      });
  };

  const handleFileSelect = () => {
    hiddenFileInput.current.click();
  };

  const handleChange = (event) => {
    if (event.target.files[0] && event.target.files[0].type.startsWith('image/')) {
      const formData = new FormData();
      formData.append('file', event.target.files[0]);
      eetch
        .userUploadImage({ accessToken: user.accessToken, refreshToken: user.refreshToken, formData })
        .then(() => {
          eetch
            .userInfo({ accessToken: user.accessToken, refreshToken: user.refreshToken })
            .then((res) => {
              setUserInfo(res.data);
            })
            .catch((err) => {
              if (err.message === 'RefreshTokenExpired') {
                dispatch(logoutUser());
                dispatch(setMenu(false));
                dispatch(setMessage(false));
                navigate(`/login`);
              }
            });
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
      setImgMessage('');
    } else {
      setImgMessage('* 이미지 파일만 업로드 가능합니다.');
    }
  };

  const submitPat = () => {
    eetch
      .githubTokenRegist({ accessToken: user.accessToken, refreshToken: user.refreshToken, personalAccessToken: pat })
      .then(() => {
        setPatMessage({ fail: false, text: '* 등록 완료' });
      })
      .catch(() => {
        setPatMessage({ fail: true, text: '* 등록 실패' });
      });
  };

  const submitChange = () => {
    eetch.userEdit({ accessToken: user.accessToken, refreshToken: user.refreshToken, nickname, statusMsg }).catch((err) => {
      deleteImage();
      if (err.message === 'RefreshTokenExpired') {
        dispatch(logoutUser());
        dispatch(setMenu(false));
        dispatch(setMessage(false));
        navigate(`/login`);
      }
    });
  };

  const deleteImage = () => {
    eetch
      .userDeleteImage({ accessToken: user.accessToken, refreshToken: user.refreshToken })
      .then(() => {
        eetch
          .userInfo({ accessToken: user.accessToken, refreshToken: user.refreshToken })
          .then((res) => {
            setUserInfo(res.data);
          })
          .catch((err) => {
            if (err.message === 'RefreshTokenExpired') {
              dispatch(logoutUser());
              dispatch(setMenu(false));
              dispatch(setMessage(false));
              navigate(`/login`);
            }
          });
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  useEffect(() => {
    const image = imageRef.current;
    const size = sizeRef.current;
    const updateMaxHeight = () => {
      size.style.maxHeight = `${image.offsetWidth}px`;
    };

    const resizeObserver = new ResizeObserver(updateMaxHeight);
    resizeObserver.observe(image);

    return () => {
      resizeObserver.disconnect();
    };
  }, []);

  return (
    <>
      <s.OutWrapper $toggle={toggle} onClick={() => setToggle(false)} />
      <s.EditWrapper $toggle={toggle} onClick={() => setToggle(false)}>
        <s.EditModalWrapper onClick={(event) => event.stopPropagation()}>
          <s.GradBoxWrapper>
            <s.MessageWrapper>
              <s.EditImageWrapper ref={sizeRef}>
                <s.ProfileImage
                  ref={imageRef}
                  src={userInfo.profileDto ? `https://k9d103.p.ssafy.io/img/user/${userInfo.profileDto.fileName}` : userStockImage}
                />
                <s.ImageButton onClick={handleFileSelect}>
                  <PhotoFilterIcon />
                </s.ImageButton>
                <s.ImageButton onClick={deleteImage}>
                  <DisabledByDefaultRoundedIcon />
                </s.ImageButton>
                <input ref={hiddenFileInput} type="file" style={{ display: 'none' }} onChange={handleChange} />
              </s.EditImageWrapper>
              <s.descriptionMessage>{imgMessage}</s.descriptionMessage>
            </s.MessageWrapper>
            <Input
              label="깃헙 엑세스 토큰 변경"
              holder={userInfo.personalAccessToken && userInfo.personalAccessToken.substr(0, 10).concat(' ···')}
              data={pat && pat}
              setData={setPat}
              enter={submitPat}
              click={submitPat}
              message={patMessage}
            />
            <Input label="닉네임" data={nickname && nickname} setData={setNickname} valid={nicknameValid} />
            <Input label="상태 메시지" data={statusMsg && statusMsg} setData={setStatusMsg} />
            <s.ButtonWrapper>
              <s.ProfileEditButton type="button" onClick={submitChange}>
                적용
              </s.ProfileEditButton>
              <s.CloseButton onClick={() => setToggle(false)}>닫기</s.CloseButton>
              <s.LogoutButton onClick={logoutHandler}>
                <MeetingRoomRoundedIcon />
                로그아웃
              </s.LogoutButton>
            </s.ButtonWrapper>
          </s.GradBoxWrapper>
          <s.GradBoxTop />
          <s.GradBoxBottom />
        </s.EditModalWrapper>
      </s.EditWrapper>
    </>
  );
};

export default EditProfile;
