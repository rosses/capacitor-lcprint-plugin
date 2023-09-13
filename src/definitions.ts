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
  header: DocHeader,
  to: DocPeople,
  from: DocPeople,
  items: DocLine[],
  totals: DocTotals,
  payment: DocPayment,
  signed: DocSigned,
  barcode: string,
  footer: string
}
export interface DocHeader {
  doctype: number,
  docname: string,
  folio: string
}
export interface DocLine {
  code: string,
  description: string,
  quantity: number,
  price: number,
  total: number
}
export interface DocPeople {
  code: string,
  name: string,
  address: string,
  county: string,
  phone: string,
  activity: string
}
export interface DocTotals {
  subtotal: number,
  discounts: number,
  net: number,
  tax: number,
  total: number
}
export interface DocSigned {
  image: string,
  text1: string,
  text2: string
}
export interface DocPayment {
  cash: number,
  card: number,
  transfers: number,
  check: number
}