"use client"

const Logo = () => {
    return (
        <div
        className='flex center-content gap:0.5vw h:100% user-select:none user-drag:none cursor:pointer opacity:1 opacity:0.5:hover transition:0.3s' 
        onClick={()=>{window.location.href=`/OnlinePDFReader`;}}>
            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/PDF_file_icon.svg/1200px-PDF_file_icon.svg.png"
            className="h:90% w:auto" />
            <span className="white-space:nowrap">Online Reader</span>
        </div>
    )
}

export default Logo;