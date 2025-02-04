"use client"
import { AppCtx } from "@/ctx";
import { useContext, useEffect, useState } from "react";

const Book = (props) => {
    const { setPdfs } = useContext(AppCtx);
    const [color, setColor] = useState();

    useEffect(()=>{
        setColor(Math.floor(Math.random()*360));
    }, []);

    return (
        <div className={`flex flex-direction:column gap:3% align-items:center w:17% h:47vh transform:scale(1.025):hover transition:0.5s mx:0.5% my:0.5cqw px:1% py:1cqw border-radius:1rem bg:linear-gradient(135deg,hsl(${color},100%,75%),white) user-select:none>* user-drag:none>*`}>
            <span className="w:calc(95%) f:20 f:bold text-align:center overflow:hidden white-space:nowrap text-overflow:ellipsis">{props.title}</span>
            <div className="flex center-content w:90% aspect-ratio:1 overflow:hidden cursor:pointer"
                 onClick={()=>{window.location.href=`/OnlinePDFReader/reader/${props.id}`;}}>
                <img className="w:auto h:100% user-select:none user-drag:none" src={`/OnlinePDFReader/pdf/thumbnail/${props.id}`} />
            </div>
            <span className="f:18">last read: page {props.page+1}</span>
            <button className="w:75% f:1.5cqw bg:blue-50 color:white border:none border:blue-75|2.5px|solid:focus border-radius:1rem cursor:pointer"
                    onClick={()=>{
                        const a = document.createElement("a");
                        a.href = `/OnlinePDFReader/pdf/file/${props.id}`;
                        a.setAttribute("download", "");
                        a.click();
                    }}>
            download</button>
            <button className="w:75% f:1.5cqw bg:red-50 color:white border:none border:red-75|2.5px|solid:focus border-radius:1rem cursor:pointer"
                    onClick={async ()=>{
                        await fetch(`/OnlinePDFReader/pdf/${props.id}`, {
                            method: "DELETE"
                        });
                        setPdfs(await getPdfs());
                    }}>
            delete</button>

        </div>
    )
}

const BookShelf = () => {
    const { menuOpen, pdfs } = useContext(AppCtx);
    return (
        <div className={`fixed top:10vh left:${menuOpen?"15vw":0} h:90vh w:${menuOpen?"85vw":"100vw"} bg:gray-70 transition:0.5s`}>
            <div className="w:90% h:100% flex flex-wrap:wrap px:5% overflow-y:scroll">
                {pdfs==null?null:pdfs.map((pdf)=>{return <Book key={pdf.id} id={pdf.id} title={pdf.name} page={pdf.atPage} totalPage={pdf.totalPage} />})}
            </div>
        </div>
    )
}

const getPdfs = async () => {
    return await (await fetch("/OnlinePDFReader/pdfs", {
        method: "GET"
    })).json();
}

export { getPdfs, BookShelf };