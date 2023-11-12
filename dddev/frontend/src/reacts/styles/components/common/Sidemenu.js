import styled from 'styled-components';
import { Link } from 'react-router-dom';

export const SidemenuWrapper = styled.aside`
  position: relative;
  z-index: 2;

  width: ${({ $menuToggle }) => ($menuToggle ? '275px' : '0px')};
  flex-shrink: 0;

  overflow: hidden;

  transition: width 0.5s ease;
`;

export const PositionWrapper = styled.div`
  position: absolute;
  top: 0;
  right: 0;

  width: 275px;
  height: 100%;

  background-color: var(--bg-lite);
  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 4px;
`;

export const MenuNav = styled.nav`
  position: relative;
  margin-top: 20px;
`;

export const MenuCategory = styled(Link)`
  width: calc(100% - 20px);
  text-decoration: none;
  display: block;
  padding: 3px 20px;
  margin-top: 10px;
  margin-left: 10px;

  display: flex;
  align-items: center;

  &:hover {
    background-color: var(--bg-lite-hover);
  }

  & > svg {
    width: 19px;
    height: 19px;
    margin-right: 4px;
  }

  border-radius: 0.5rem;
`;

export const MenuChild = styled(Link)`
  width: calc(100% - 20px);

  text-decoration: none;
  padding: 3px 20px 3px 40px;
  display: block;
  margin-left: 10px;

  display: flex;
  align-items: center;

  &:hover {
    background-color: var(--bg-lite-hover);
  }

  & > svg {
    width: 19px;
    height: 19px;
    margin-right: 4px;
  }

  border-radius: 0.5rem;
`;

export const GroundList = styled.ul`
  position: absolute;
  top: 58px;
  left: 10px;

  width: 255px;

  visibility: ${({ $groundListToggle }) => ($groundListToggle ? 'visible' : 'hidden')};
  opacity: ${({ $groundListToggle }) => ($groundListToggle ? '1' : '0')};

  transform: ${({ $groundListToggle }) => ($groundListToggle ? 'scale(1)' : 'scale(0.95)')};
  background-color: var(--bg-dark);
  padding: 10px;

  list-style: none;

  transition: all 0.5s ease;

  border-radius: 0.5rem;

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 4px;

  & > .addGround {
    margin-top: 15px;
    list-style-type: none;
  }

  & > div {
    position: absolute;
    height: calc(100% - 20px);
    width: 5px;
    top: 10px;

    border-radius: 1px;

    left: -8px;

    background-color: var(--font-toggle);
  }
`;

export const GroundItem = styled.li`
  color: var(--font-rev);
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  padding: 3px 10px;

  border-radius: 0.25rem;

  &:hover {
    background-color: var(--bg-lite-hover);
  }

  cursor: pointer;
`;

export const BottomWrapper = styled.div`
  margin-top: 30px;
`;

export const MenuButton = styled.button`
  width: calc(100% - 20px);
  display: block;
  color: var(--font-rev);

  font-size: 18px;
  padding: 3px 20px;
  margin-left: 10px;

  background-color: transparent;
  border: none;
  cursor: pointer;

  display: flex;
  align-items: center;

  &:hover {
    background-color: var(--bg-lite-hover);
  }
  border-radius: 0.5rem;

  & > svg {
    width: 19px;
    height: 19px;
    margin-right: 4px;
  }
`;
