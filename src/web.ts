import { WebPlugin } from '@capacitor/core';

import type { LcprintPlugin } from './definitions';

export class LcprintWeb extends WebPlugin implements LcprintPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
