
Vagrant.configure("2") do |config|

  config.vm.box = "centos/7"
  config.vm.network "private_network", ip: "192.168.33.11"
  config.vm.synced_folder ".", "/vagrant", disabled: true
  config.vm.synced_folder "./vendingMachine", "/home/vagrant/app", owner: "vagrant", group: "vagrant"
  config.vm.synced_folder "./db", "/home/vagrant/db", owner: "vagrant", group: "vagrant"
#  config.vm.synced_folder "./vendingMachine-view/dist", "/usr/share/nginx/html", owner: "root", group: "root"
  config.vm.synced_folder "./conf/nginx/repo", "/home/vagrant/repo", owner: "root", group: "root"
  config.vm.synced_folder "./conf/nginx/conf.d", "/home/vagrant/conf/nginx", owner: "root", group: "root"
  config.vm.synced_folder "./conf/selinux", "/home/vagrant/conf/selinux", owner: "root", group: "root"
  $provision_scripts = <<-"EOF"

    # nginx install
    which nginx
    if [ "$?" -eq 1 ]; then
      cp -f /home/vagrant/repo/nginx.repo /etc/yum.repos.d/nginx.repo
      yum install -y nginx
      cp -f /home/vagrant/conf/nginx/default.conf /etc/nginx/conf.d/default.conf
      systemctl start nginx.service
      systemctl enable nginx.service
    fi

    # java install
    which java
    if [ "$?" -eq 1 ]; then
      yum install -y java-1.8.0-openjdk-devel
    fi

    # mvn install
    which mvn
    if [ "$?" -eq 1 ]; then
      cd /usr/local
      curl -OL https://archive.apache.org/dist/maven/maven-3/3.5.2/binaries/apache-maven-3.5.2-bin.tar.gz
      tar -xzvf apache-maven-3.5.2-bin.tar.gz
      mv apache-maven-3.5.2/ maven/
      rm -f apache-maven-3.5.2-bin.tar.gz
    fi

    # mysqld install
    which mysqld
    if [ "$?" -eq 1 ]; then
      yum -y localinstall http://dev.mysql.com/get/mysql57-community-release-el6-7.noarch.rpm
      yum-config-manager --disable mysql57-community
      yum-config-manager --enable mysql56-community
      yum -y install mysql-community-server
      mysqld --version
      systemctl start mysqld.service
      systemctl enable mysqld.service
      mysql -u root -e "GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';"
      mysql -u root -e "CREATE DATABASE IF NOT EXISTS vending_machine;"
    fi

    # set maven path
    cat /etc/profile | grep "M2_HOME"
    if [ "$?" -eq 1 ]; then
      echo 'export M2_HOME=/usr/local/maven' >> /etc/profile
      echo 'export M2=$M2_HOME/bin' >> /etc/profile
      echo 'export PATH=$M2:$PATH' >> /etc/profile
      source /etc/profile
    fi

    # db migrate
    cd /home/vagrant/db
    mvn flyway:migrate

    # permissive SELinux
    setenforce 0

  EOF
  config.vm.provision :shell, :inline => $provision_scripts

end
