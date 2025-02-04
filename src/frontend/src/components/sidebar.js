"use client"
import { AppCtx } from "@/ctx";
import { HomeIcon, UploadIcon } from "@radix-ui/react-icons";
import { useContext, useState } from "react";
import { getPdfs } from "@/components/bookShelf";

let sleepSetTimeoutCtrl;
function sleep(ms) {
    clearInterval(sleepSetTimeoutCtrl);
    return new Promise(resolve => sleepSetTimeoutCtrl = setTimeout(resolve, ms));
}

function uploadPDF(setUploadingState, setPdfs) {
    const uploadInp = document.createElement("input");
    uploadInp.type = "file";
    uploadInp.accept = ".pdf";
    uploadInp.addEventListener("change", async function() {
        const file = this.files[0];
        if (file) {
            const formData = new FormData();
            formData.append("uploadedFile", file);

            setUploadingState({
                state: "uploading"
            });
            let resp = await (await fetch("/OnlinePDFReader/pdf", {
                method: "POST", 
                body: formData
            })).json()

            if (resp.status=="Accepted") {
                setUploadingState({
                    state: "working", 
                    progress: "0"
                });
                const token = resp.token;
                while (true) {
                        resp = await (await fetch(`/OnlinePDFReader/task/${token}`, {
                        method: "GET"
                    })).json()

                    setUploadingState({
                        state: "working", 
                        progress: resp.progress
                    });

                    if (resp.state!=="working") {
                        break;
                    }
                    await sleep(1000);
                }

                if (resp.state==="finished") {
                    setUploadingState(undefined);
                    setPdfs(await getPdfs());
                }
                else {
                    alert("file processing failed!");
                }
            }

            else {
                alert("file upload failed!");
            }
        }
    });
    uploadInp.click();
}

const Sidebar = () => {
    const [uploadingState, setUploadingState] = useState(undefined);
    const { menuOpen, accountId, setPdfs } = useContext(AppCtx);

    return (
        <div className={`fixed top:10vh bottom:0 left:${menuOpen?0:"-15vw"} visibility:${menuOpen?"visible":"hidden"} w:15vw bg:gray-75 border-top-right-radius:8px border-bottom-right-radius:8px transition:0.5s transition:0.5s>div`}>
            <div className="flex center-content gap:5% m:1% w-90% h:10% border-bottom:gray-70|1px|solid bg:gray-80:hover border-radius:12px:hover font:24 cursor:pointer"
                 onClick={()=>{window.location.href="/OnlinePDFReader";}}>
                <HomeIcon />
                <span>Index</span>
            </div>
            <div className={`flex center-content gap:5% m:1% w-90% h:10% border-bottom:gray-70|1px|solid bg:gray-80:hover border-radius:12px:hover font:24 cursor:${uploadingState!=undefined||accountId==="-1"?"not-allowed":"pointer"} ${uploadingState!=undefined||accountId==="-1"?"opacity:0.4":""}`}
                 onClick={()=>{uploadingState!=undefined||accountId==="-1"?null:uploadPDF(setUploadingState, setPdfs);}}>
                <UploadIcon />
                {uploadingState==undefined?"Upload PDF":
                 uploadingState.state==="uploading"?"Uploading PDF...":"Converting ".concat(uploadingState.progress).concat("%")}
            </div>
        </div>
    )
}

export default Sidebar;