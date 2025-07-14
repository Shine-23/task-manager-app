import Navbar from './components/Navbar'

function Layout({ children }) {
  return (
    <>
      <Navbar />
      <main style={{ backgroundColor: 'white', minHeight: '100vh' }}>
        {children}
      </main>
    </>
  )
}

export default Layout
