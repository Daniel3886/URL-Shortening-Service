// 'use client'
// import React from 'react'
// import { cn } from '@/lib/utils'

// const menuItems = [
//     { name: 'Features', href: '#link' },
//     { name: 'Solution', href: '#link' },
//     { name: 'Pricing', href: '#link' },
//     { name: 'About', href: '#link' },
// ]

// export const HeroHeader = () => {
//     const [menuState, setMenuState] = React.useState(false)
//     const [isScrolled, setIsScrolled] = React.useState(false)

//     React.useEffect(() => {
//         const handleScroll = () => {
//             setIsScrolled(window.scrollY > 50)
//         }
//         window.addEventListener('scroll', handleScroll)
//         return () => window.removeEventListener('scroll', handleScroll)
//     }, [])
//     return (
//         <header>
//             <nav
//                 data-state={menuState && 'active'}
//                 className="fixed z-20 w-full px-2">
//                 <div className={cn('mx-auto mt-2 max-w-6xl px-6 transition-all duration-300 lg:px-12', isScrolled && 'bg-background/50 max-w-4xl rounded-2xl border backdrop-blur-lg lg:px-5')}>
//                     <div className="relative flex flex-wrap items-center justify-between gap-6 py-3 lg:gap-0 lg:py-4">
//                         <div className="flex w-full justify-between lg:w-auto">
//                             {/* <svg width="24" height="24" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><path stroke-width="3" d="M5 10h.01M5 16h.01"/><path stroke-width="2" d="m10 17l4-10m2 10l4-10"/></g></svg> */}
//                             <svg width="32" height="32" viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg"><path fill="#42a5f5" d="M10 14h12v4H10z"/><path fill="#42a5f5" d="M12 22H9.562A5.57 5.57 0 0 1 4 16.438v-.876A5.57 5.57 0 0 1 9.562 10H12V6H9.562A9.56 9.56 0 0 0 0 15.562v.876A9.56 9.56 0 0 0 9.562 26H12ZM22.438 6H20v4h2.438A5.57 5.57 0 0 1 28 15.562v.876A5.57 5.57 0 0 1 22.438 22H20v4h2.438A9.56 9.56 0 0 0 32 16.438v-.876A9.56 9.56 0 0 0 22.438 6"/></svg>
//                             {/* <Link
//                                 href="/"
//                                 aria-label="home"
//                                 className="flex items-center space-x-2">
//                                 <Logo />
//                             </Link> */}

//                             {/* <button
//                                 onClick={() => setMenuState(!menuState)}
//                                 aria-label={menuState == true ? 'Close Menu' : 'Open Menu'}
//                                 className="relative z-20 -m-2.5 -mr-4 block cursor-pointer p-2.5 lg:hidden">
//                                 <Menu className="in-data-[state=active]:rotate-180 in-data-[state=active]:scale-0 in-data-[state=active]:opacity-0 m-auto size-6 duration-200" />
//                                 <X className="in-data-[state=active]:rotate-0 in-data-[state=active]:scale-100 in-data-[state=active]:opacity-100 absolute inset-0 m-auto size-6 -rotate-180 scale-0 opacity-0 duration-200" />
//                             </button> */}
//                         </div>

//                         {/* <div className="absolute inset-0 m-auto hidden size-fit lg:block">
//                             <ul className="flex gap-8 text-sm">
//                                 {menuItems.map((item, index) => (
//                                     <li key={index}>
//                                         <Link
//                                             href={item.href}
//                                             className="text-muted-foreground hover:text-accent-foreground block duration-150">
//                                             <span>{item.name}</span>
//                                         </Link>
//                                     </li>
//                                 ))}
//                             </ul>
//                         </div> */}

//                         {/* <div className="bg-background in-data-[state=active]:block lg:in-data-[state=active]:flex mb-6 hidden w-full flex-wrap items-center justify-end space-y-8 rounded-3xl border p-6 shadow-2xl shadow-zinc-300/20 md:flex-nowrap lg:m-0 lg:flex lg:w-fit lg:gap-6 lg:space-y-0 lg:border-transparent lg:bg-transparent lg:p-0 lg:shadow-none dark:shadow-none dark:lg:bg-transparent">
//                             <div className="lg:hidden">
//                                 <ul className="space-y-6 text-base">
//                                     {menuItems.map((item, index) => (
//                                         <li key={index}>
//                                             <Link
//                                                 href={item.href}
//                                                 className="text-muted-foreground hover:text-accent-foreground block duration-150">
//                                                 <span>{item.name}</span>
//                                             </Link>
//                                         </li>
//                                     ))}
//                                 </ul>
//                             </div> */}
//                             {/* <div className="flex w-full flex-col space-y-3 sm:flex-row sm:gap-3 sm:space-y-0 md:w-fit">
//                                 <Button
//                                     asChild
//                                     variant="outline"
//                                     size="sm"
//                                     className={cn(isScrolled && 'lg:hidden')}>
//                                     <Link href="#">
//                                         <span>Login</span>
//                                     </Link>
//                                 </Button>
//                                 <Button
//                                     asChild
//                                     size="sm"
//                                     className={cn(isScrolled && 'lg:hidden')}>
//                                     <Link href="#">
//                                         <span>Sign Up</span>
//                                     </Link>
//                                 </Button>
//                                 <Button
//                                     asChild
//                                     size="sm"
//                                     className={cn(isScrolled ? 'lg:inline-flex' : 'hidden')}>
//                                     <Link href="#">
//                                         <span>Get Started</span>
//                                     </Link>
//                                 </Button>
//                             </div> */}
//                         {/* </div> */}
//                     </div>
//                 </div>
//             </nav>
//         </header>
//     )
// }
