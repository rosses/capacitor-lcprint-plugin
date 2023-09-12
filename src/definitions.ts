export interface LcprintPlugin {
  start(): Promise<{ success: boolean }>;
  printText(options: { size: number, bold: boolean, underline: boolean, value: string }): Promise<{ success: boolean }>;
  feed(options: {lines: number}): Promise<{ success: boolean }>;
  printBarcode128(options: { code: string }): Promise<{ success: boolean }>;
  printImageUrl(options: { url: string }): Promise<{ success: boolean }>;
  voucher(options: { receipt: Doc }): Promise<{ success: boolean }>;
  print(): Promise<{ success: boolean }>;
}
export interface Doc {
  header: {
    docname: string
  }
}