import { BookShelf } from "@/components/bookShelf"
import Header from "@/components/header"
import Sidebar from "@/components/sidebar"

export default function Index() {
  return (
    <>
      <Header index={true}/>
      <Sidebar />
      <BookShelf />
    </>
  )
}