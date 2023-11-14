import * as s from 'reacts/styles/components/common/Modal';

const ModalChanger = ({ isOpen, type, accept, message, onRequestClose }) => {
  return (
    <>
      <s.ModalWrapper isOpen={isOpen} />
      <s.ModalChanger isOpen={isOpen} contentLabel="Custom Modal" overlayClassName="reactModalOverlayCustom" onRequestClose={onRequestClose}>
        <p>{message}</p>
        {type === 'check' && (
          <s.Button className="accept-button" onClick={accept}>
            확인
          </s.Button>
        )}
        {type === 'check' && <s.Button onClick={onRequestClose}>취소</s.Button>}
        {type === 'alarm' && <s.Button onClick={onRequestClose}>확인</s.Button>}
      </s.ModalChanger>
    </>
  );
};

export default ModalChanger;
