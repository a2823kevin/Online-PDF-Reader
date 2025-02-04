import { CSSProvider } from '@master/css.react'
import config from "@/../master.css"
import { AppCtxProvider } from "@/ctx";

export const metadata = {
  title: "Reader",
}

export default function RootLayout({ children }) {
  return (
    <html lang="zh-TW">
      <body>
        <AppCtxProvider>
          <CSSProvider config={config}>
            {children}
          </CSSProvider>
        </AppCtxProvider>
      </body>
    </html>
  )
}