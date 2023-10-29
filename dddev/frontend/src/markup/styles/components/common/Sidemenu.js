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

  background-color: aqua;
`;

export const MenuNav = styled.nav`
  position: relative;
  margin-top: 50px;
`;

export const MenuCategory = styled(Link)`
  display: block;
`;

export const MenuChild = styled(Link)`
  display: block;
`;

export const GroundList = styled.ul`
  position: absolute;
  top: 30px;
  left: 0;

  width: 275px;
  transform: translateY(${({ $groundListToggle }) => ($groundListToggle ? '-30px' : '-100%')});

  background-color: white;
  padding-bottom: 30px;

  transition: transform 0.5s ease;

  & > div {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 275px;
    height: 30px;

    background-color: #aaa;

    cursor: pointer;
  }
`;

export const GroundItem = styled.li`
  cursor: pointer;
`;
