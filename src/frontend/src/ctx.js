"use client"
import { createContext, useEffect, useState } from "react";
import { getPdfs } from "@/components/bookShelf";

const AppCtx = createContext({
    pdfs: [], 
    setPdfs: ()=>{}, 
    menuOpen: false, 
    toggleMenu: ()=>{}, 
    accountId: -1, 
    setAccountId: ()=>{}, 
    totalPage: -1, 
    setTotalPage: ()=>{}, 
    atPage: -1, 
    setAtPage: ()=>{}, 
    userTyping: false, 
    setUserTyping: ()=>{}
});

const AppCtxProvider = ({ children }) => {
    const [pdfs, setPdfs] = useState([]);
    const [menuOpen, setMenuOpen] = useState(false);
    const [accountId, setAccountId] = useState(-1);
    const [totalPage, setTotalPage] = useState(-1);
    const [atPage, setAtPage] = useState(-1);
    const [userTyping, setUserTyping] = useState(false);

    const toggleMenu = () => {
        menuOpen?setMenuOpen(false):setMenuOpen(true);
    }

    useEffect(()=>{
        const updatePdf = async () => {
            console.log(await getPdfs());
            setPdfs(await getPdfs());
        }
        updatePdf();
    }, [accountId])

    return (
        <AppCtx.Provider value={{pdfs, setPdfs, menuOpen, toggleMenu, accountId, setAccountId, totalPage, setTotalPage, atPage, setAtPage, userTyping, setUserTyping}}>
            {children}
        </AppCtx.Provider>
    )
}

export { AppCtx, AppCtxProvider };