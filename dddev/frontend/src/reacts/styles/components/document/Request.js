import styled from 'styled-components';

export const RequestWrapper = styled.div``;

export const RequestListWrapper = styled.div`
  width: 300px;

  & > div {
    margin-left: 5px;
    margin-top: 12px;
    margin-bottom: 0px;
    & > div {
      width: 270px;
      margin: 0;
    }

    & > ul {
      margin: 16px 12.5px 8px; 12.5px;
    }
  }
`;

export const WriterWrapper = styled.div`
  @media (max-width: 682px) {
    position: relative;

    padding: 0 15px 0 10px;
  }
  @media (min-width: 683px) {
    position: absolute;
    left: 285px;
    padding: 0 15px 0 10px;
    top: 0;
    width: calc(100% - 285px);
  }
`;

export const RequestList = styled.ul`
  width: 270px;
  padding: 9px;
  margin: 0 15px 15px 5px;
  height: fit-content;

  max-height: calc(100vh - 213px);
  overflow-y: auto;

  list-style: none;

  border-radius: 0.75rem;
  background-color: var(--bg-lite);
  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;
`;
export const RequestItem = styled.li`
  padding-bottom: 9px;

  cursor: pointer;
`;

export const ItemWrapper = styled.div`
  padding: 10px;
  border-radius: 0.5rem;

  &:hover {
    background-color: var(--bg-basic);
  }
`;

export const ItemAuthor = styled.span`
  font-size: 0.9rem;

  display: flex;
  align-items: center;
  margin-bottom: 10px;
`;
export const ItemTitle = styled.h2`
  font-size: 1.2rem;
  padding: 0 5px;
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
`;

export const ThereIsNothing = styled.div`
  position: absolute;
  width: 375px;
  text-align: center;

  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

export const ToDocsButton = styled.button`
  display: block;
  margin: 0 auto;
  height: 40px;
  padding: 0 15px;
  margin-top: 10px;

  border: none;
  border-radius: 0.75rem;
  background-color: transparent;
  color: var(--font-rev);
  font-size: 1rem;

  display: flex;
  align-items: center;

  cursor: pointer;

  &:hover {
    background-color: var(--button-basic);
  }

  & > svg {
    margin-left: 5px;
    width: 21px;
    height: 21px;
  }
`;
