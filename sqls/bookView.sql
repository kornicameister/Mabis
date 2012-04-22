SELECT  b.idBook,
        b.isbn,
        b.titleOriginal,
        b.titleLocale,
        b.pages as bookPages,
        g.genre as bookGenre,
        p.image as bookCover,
        a.idAuthor as authorId,
        a.firstName as authorFirstName,
        a.lastName as authorLastName,
        a.picture as authorAvatar
        FROM mabis.book as b    inner join mabis.genre as g on b.genre=g.idGenre 
                                inner join mabis.picture p on b.cover=p.idPicture 
                                inner join mabis.author a on b.writer = a.idAUthor
        group by b.isbn order by a.idAuthor asc;