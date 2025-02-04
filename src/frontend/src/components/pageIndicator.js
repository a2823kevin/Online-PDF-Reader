"use client"
import { AppCtx } from "@/ctx";
import * as Tooltip from "@radix-ui/react-tooltip";
import { useContext } from "react";

const PageIndicator = (props) => {
    const { totalPage, atPage, setAtPage, setUserTyping } = useContext(AppCtx);
    return (props.index?<></>
    :
    <div className='flex gap:0.5vw center-content f:20'>
        {totalPage===-1?
        <>
            {"loading pages..."}
        </>:
        <>
            <Tooltip.Provider>
                    <Tooltip.Root>
                        <Tooltip.Trigger asChild>
                            <input type='text' className='w:3vw h:5vh px:0 text:center border-radius:8px border:none outline:none color:#8B0000 f:20 f:Arial, Helvetica, sans-serif f:bold'
                            value={atPage+1}
                            onChange={(evt)=>{setAtPage(evt.target.value-1);}}
                            onFocus={(evt)=>{setUserTyping(true)}}
                            onBlur={(evt)=>{setUserTyping(false)}} />
                        </Tooltip.Trigger>
                        <Tooltip.Portal>
                            <Tooltip.Content>
                                <span className='border-radius:4px bg:black color:white'>Enter a page number</span>
                                <Tooltip.Arrow />
                            </Tooltip.Content>
                        </Tooltip.Portal>
                    </Tooltip.Root>
            </Tooltip.Provider>
            <span>/</span>
            <span>{totalPage}</span>
        </>
        }
    </div>)
}

export default PageIndicator;