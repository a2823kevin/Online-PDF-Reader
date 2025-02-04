import Header from "@/components/header"
import PdfContainer from "@/components/pdfContainer"
import Sidebar from "@/components/sidebar"

export default function Reader() {
  return (
    <>
      <Header index={false} />
      <Sidebar />
      <PdfContainer />
    </>
  )
}