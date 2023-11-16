import * as user from 'eetch/user';
import * as repo from 'eetch/repo';
import * as ground from 'eetch/ground';
import * as alert from 'eetch/alert';

const eetch = async (url, options, refreshToken) => {
  const res = await fetch(url, options);
  if (res.status === 403) {
    const newAccessToken = await fetch(`https://k9d103.p.ssafy.io:8001/oauth/re-issue`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization-refresh': refreshToken,
      },
    });

    if (!newAccessToken.ok) throw new Error(`RefreshTokenExpired`);

    options.accessToken = newAccessToken;
    const retry = await fetch(url, options);
    if (!retry.ok) throw new Error(`${retry.status} 에러`);

    return retry;
  }

  if (!res.ok && res.status !== 403) {
    throw new Error(`${res.status} 에러`);
  }

  return res;
};

eetch.addDeviceToken = (values) => alert.addDeviceToken(values);
eetch.alertList = (values) => alert.alertList(values);
eetch.createAlert = (values) => alert.createAlert(values);
eetch.updateAlert = (values) => alert.updateAlert(values);
eetch.deleteAlert = (values) => alert.deleteAlert(values);
eetch.getAlert = (values) => alert.getAlert(values);

eetch.githubSync = (values) => user.githubSync(values);
eetch.githubTokenRegist = (values) => user.githubTokenRegist(values);
eetch.githubTokenRegist = (values) => user.githubTokenRegist(values);
eetch.userEdit = (values) => user.userEdit(values);
eetch.userInfo = (values) => user.userInfo(values);
eetch.userNickname = (values) => user.userNickname(values);
eetch.userGrounds = (values) => user.userGrounds(values);
eetch.changeLastGround = (values) => user.changeLastGround(values);
eetch.userProfileImage = (values) => user.userProfileImage(values);
eetch.userUploadImage = (values) => user.userUploadImage(values);
eetch.userDeleteImage = (values) => user.userDeleteImage(values);

eetch.repoList = (values) => repo.repoList(values);

eetch.createGround = (values) => ground.createGround(values);
eetch.getGround = (values) => ground.getGround(values);
eetch.editGround = (values) => ground.editGround(values);
eetch.createDocument = (values) => ground.createDocument(values);
eetch.listDocument = (values) => ground.listDocument(values);
eetch.linkDocument = (values) => ground.linkDocument(values);
eetch.treeDocument = (values) => ground.treeDocument(values);
eetch.parentsList = (values) => ground.parentsList(values);
eetch.detailDocument = (values) => ground.detailDocument(values);
eetch.editDocument = (values) => ground.editDocument(values);
eetch.statusDocument = (values) => ground.statusDocument(values);
eetch.senderDocument = (values) => ground.senderDocument(values);
eetch.receiverDocument = (values) => ground.receiverDocument(values);
eetch.commentDocument = (values) => ground.commentDocument(values);
eetch.timeDocument = (values) => ground.timeDocument(values);
eetch.titleDocument = (values) => ground.titleDocument(values);
eetch.deleteDocument = (values) => ground.deleteDocument(values);
eetch.groundUsers = (values) => ground.groundUsers(values);
eetch.groundUser = (values) => ground.groundUser(values);
eetch.groundOwn = (values) => ground.groundOwn(values);
eetch.groundInvite = (values) => ground.groundInvite(values);
eetch.groundOut = (values) => ground.groundOut(values);
eetch.createSprint = (values) => ground.createSprint(values);
eetch.listSprint = (values) => ground.listSprint(values);
eetch.sprintIssues = (values) => ground.sprintIssues(values);
eetch.listRequest = (values) => ground.listRequest(values);
eetch.recentSprint = (values) => ground.recentSprint(values);
eetch.startSprint = (values) => ground.startSprint(values);
eetch.completeSprint = (values) => ground.completeSprint(values);
eetch.connectSprint = (values) => ground.connectSprint(values);
eetch.multiConnectSprint = (values) => ground.multiConnectSprint(values);
eetch.editSprint = (values) => ground.editSprint(values);
eetch.deleteSprint = (values) => ground.deleteSprint(values);
eetch.activeCount = (values) => ground.activeCount(values);
eetch.activeTime = (values) => ground.activeTime(values);
eetch.burnDown = (values) => ground.burnDown(values);
eetch.focusCount = (values) => ground.focusCount(values);
eetch.focusTime = (values) => ground.focusTime(values);
eetch.totalCount = (values) => ground.totalCount(values);
eetch.totalTime = (values) => ground.totalTime(values);
eetch.issueToggle = (values) => ground.issueToggle(values);
eetch.generateToken = (values) => ground.generateToken(values);
eetch.recentLog = (values) => ground.recentLog(values);
eetch.keywordLog = (values) => ground.keywordLog(values);
eetch.regexLog = (values) => ground.regexLog(values);
eetch.gptSolution = (values) => ground.gptSolution(values);
eetch.addFilter = (values) => ground.addFilter(values);
eetch.getFilter = (values) => ground.getFilter(values);
eetch.removeFilter = (values) => ground.removeFilter(values);

export default eetch;
