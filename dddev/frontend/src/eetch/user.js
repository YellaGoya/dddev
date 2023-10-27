import eetch from 'eetch/eetch';

export const githubSync = async ({ code }) => {
  try {
    const url = `https://k9d103.p.ssafy.io/oauth/sign-in?code=${code}`;
    const options = {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
    };

    return await eetch(url, options);
  } catch (error) {
    console.log(error);
  }
};
