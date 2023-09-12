import { registerPlugin } from '@capacitor/core';

import type { LcprintPlugin } from './definitions';

const Lcprint = registerPlugin<LcprintPlugin>('Lcprint', {
  web: () => import('./web').then(m => new m.LcprintWeb()),
});

export * from './definitions';
export { Lcprint };
