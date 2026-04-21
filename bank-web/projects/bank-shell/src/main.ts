import { loadRemoteEntry } from '@angular-architects/module-federation';

import { ADMIN_REMOTE_ENTRY } from './app/remote-config';

Promise.all([
  loadRemoteEntry({
    type: 'module',
    remoteEntry: ADMIN_REMOTE_ENTRY,
  }),
])
  .catch((err) => console.error('Error loading remote entries', err))
  .then(() => import('./bootstrap'))
  .catch((err) => console.error(err));
