import styled from 'styled-components';

export const UsersWrapper = styled.div`
  position: relative;
  margin-bottom: 1.5rem;
`;

export const Label = styled.div`
  position: absolute;
  left: 40px;
  top: -9px;
  padding: 0 5px;
  color: var(--font-border);
  background-color: var(--bg-dark);

  font-weight: 500;
  font-size: 0.8rem;
`;
export const List = styled.div`
  width: calc(100% - 50px);
  margin: 0 25px;
  padding: 10px 0;

  border-radius: 0.75rem;
  border: 1px solid ${({ $isActive }) => ($isActive ? 'var(--border-focus)' : 'var(--border-basic)')};
  background-color: transparent;

  overflow: hidden;

  margin-bottom: 27px;

  &:focus {
    outline: none;
  }
`;

export const Users = styled.ul`
  max-height: 300px;
  padding: 7px 0 7px 10px;
  list-style: none;

  overflow-y: scroll;

  &:hover {
    // ul의 스크롤바 색상 파란색으로
    &::-webkit-scrollbar-thumb {
      background-color: var(--thumb-hover);
    }

    &::-webkit-scrollbar-thumb:hover {
      background-color: var(--font-rev);
    }
  }

  & > li:first-child {
    cursor: pointer;
  }
`;

export const User = styled.li`
  position: relative;
  padding: 10px;

  border-radius: 0.5rem;

  display: flex;
  align-items: center;

  &:hover {
    background-color: var(--bg-lite-hover);

    & > img {
      border: 1px solid transparent;
    }
  }

  & > svg {
    width: 21px;
    height: 21px;
  }

  .ownerIcon {
    margin-left: 5px;
    fill: var(--font-toggle);
  }
  .ownerText {
    position: absolute;
    font-size: 0.8rem;
    right: 10px;
    font-weight: 200;
    opacity: 0.5;
  }
`;

export const RemoveButton = styled.button`
  position: absolute;
  right: 10px;

  background-color: transparent;
  border: none;

  padding: 0;

  display: flex;
  align-items: center;

  font-size: 0.8rem;
  font-weight: 200;
  opacity: 0.5;

  transition: opacity 0.2s ease;

  & > svg {
    width: 21px;
    height: 21px;
  }

  cursor: pointer;

  &:hover {
    opacity: 1;
  }
`;

export const ProfileImage = styled.img`
  width: 90px;
  height: 30px;

  margin-right: 10px;
  border: 1px solid var(--border-dark);

  //비율 유지
  object-fit: cover;

  transition: filter 0.3s ease;
  border-radius: 25px;
  cursor: pointer;
`;
