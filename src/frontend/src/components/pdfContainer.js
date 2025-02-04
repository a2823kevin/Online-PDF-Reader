"use client"
import { AppCtx } from "@/ctx";
import { useContext, useEffect, useRef, useState } from "react"

const PdfContainer = () => {
    const refr = useRef(null);
    const { accountId, menuOpen, totalPage, setTotalPage, atPage, setAtPage, userTyping } = useContext(AppCtx);
    const [displayingPages, setDisplayingPages] = useState([]);

    useEffect(() => {
        async function getPdf() {
            const pathInfo = window.location.pathname.split("/OnlinePDFReader/reader");
            const pdfId = pathInfo[pathInfo.length-1].substring(1);
            const resp = await (await fetch(`/OnlinePDFReader/pdf/${pdfId}`, {method: "GET"})).json();

            const startPage = parseInt(resp.atPage);
            setAtPage(startPage);
            const pdfContent = new DOMParser().parseFromString(resp.htmlText, "text/html");

            // append head content
            pdfContent.head.querySelectorAll("style").forEach((node)=>{
                document.head.appendChild(node);
            });

            // append body content
            pdfContent.body.querySelectorAll("#page-container").forEach((node)=>{
                for (let i=0; i<node.children.length; i++) {
                    // init
                    if (i<Math.max(0, startPage) || i>startPage+5) {
                        node.children[i].classList.add("display:none");
                    }
                    node.children[i].addEventListener("mouseenter", ()=>{
                        setAtPage(i);
                    });
                }

                refr.current.appendChild(node);
                setTotalPage(node.children.length);

                //scroll to start page
                node.children[startPage].scrollIntoView();
                for (let j=startPage-5; j<startPage; j++) {
                    node.children[j].classList.remove("display:none");
                }
            });
        }
        getPdf();
    }, []);

    useEffect(()=>{
        if (atPage<0) {setAtPage(0); return;}
        if (atPage>totalPage-1) {setAtPage(totalPage-1); return;}

        // change page record
        if (accountId!==-1) {
            const pathInfo = window.location.pathname.split("/OnlinePDFReader/reader");
            const pdfId = pathInfo[pathInfo.length-1].substring(1);
            fetch(`/OnlinePDFReader/pdf/${pdfId}`, {
                method: "PUT", 
                body: JSON.stringify({
                    atPage: atPage
                })
            })
        }
        
        const newDisplayPages = Array.from({length:Math.min(atPage+5, totalPage)-Math.max(0, atPage-5)}, (_, i)=>{return Math.max(0, atPage-5)+i})
        document.body.querySelectorAll("#page-container").forEach((node) => {
            // hide pages not in +-5
            for (const pageNum of displayingPages) {
                if (!newDisplayPages.includes(pageNum)) {
                    node.children[pageNum].classList.add("display:none");
                }
            }

            // show pages in +-5
            for (const pageNum of newDisplayPages) {
                node.children[pageNum].classList.remove("display:none");
            }

            if (userTyping) {
                node.children[atPage].scrollIntoView();
            }
        });

        setDisplayingPages(newDisplayPages);
    }, [atPage])

    return (
        <>
            <div className={`fixed top:10vh left:${menuOpen?"15vw":0} h:90vh w:${menuOpen?"85vw":"100vw"} transition:0.5s`} ref={refr}></div>
        </>
    )
}

export default PdfContainer;