import { WebPlugin } from '@capacitor/core';

import type { Doc, LcprintPlugin } from './definitions';

export class LcprintWeb extends WebPlugin implements LcprintPlugin {
  async start(): Promise<{ success: boolean }> {
    return {success: true};
  }
  async printText(options: { size: number, bold: boolean, underline: boolean, value: string }): Promise<{ success: boolean }> {
    console.log('printText', options);
    return {success: true};
  } 
  async feed(options: {lines: number}): Promise<{ success: boolean }> {
    console.log('feed', options);
    return {success: true};
  }
  async printBarcode128(options: { code: string }): Promise<{ success: boolean }> {
    console.log('printBarcode128', options);
    return {success: true};
  }
  async printImageUrl(options: { url: string }): Promise<{ success: boolean }> {
    console.log('printImageUrl', options);
    return {success: true};
  }
  async voucher(options: { receipt: Doc }): Promise<{ success: boolean }> {
    console.log('voucher', options);
    return {success: true};
  }
  async print(): Promise<{ success: boolean }> {
    return {success: true};
  }
}
